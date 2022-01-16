package svenhjol.charm.module.quick_replant;

import com.mojang.brigadier.StringReader;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Right-click with a hoe to quickly harvest and replant a fully-grown crop.")
public class QuickReplant extends CharmModule {
    public static final ResourceLocation TRIGGER_REPLANTED_CROPS = new ResourceLocation(Charm.MOD_ID, "replanted_crops");
    private static final List<BlockState> REPLANTABLE = new ArrayList<>();

    @Config(name = "Replantable blockstates", description = "List of blockstate definitions for replantable crops.")
    public static List<String> configBlockStateDefinitions = Arrays.asList(
        "minecraft:beetroots[age=3]",
        "minecraft:carrots[age=7]",
        "minecraft:nether_wart[age=3]",
        "minecraft:potatoes[age=7]",
        "minecraft:wheat[age=7]"
    );

    @Override
    public void runWhenEnabled() {
        for (String definition : configBlockStateDefinitions) {
            tryAddReplantable(definition);
        }

        UseBlockCallback.EVENT.register(this::tryHarvest);
    }

    public InteractionResult tryHarvest(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        // event is broken in fabric? hand is always mainhand
        ItemStack mainhand = player.getMainHandItem();
        ItemStack offhand = player.getOffhandItem();
        ItemStack held;

        if (mainhand.getItem() instanceof HoeItem) {
            held = mainhand;
        } else if (offhand.getItem() instanceof HoeItem) {
            held = offhand;
        } else {
            held = null;
        }

        if (held != null) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (!REPLANTABLE.contains(state)) {
                return InteractionResult.PASS;
            }

            Item blockItem = block.asItem();
            BlockState newState = block.defaultBlockState();

            if (!level.isClientSide) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                ServerLevel serverLevel = (ServerLevel)serverPlayer.level;

                List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, null, player, ItemStack.EMPTY);
                for (ItemStack drop : drops) {
                    if (drop.getItem() == blockItem) {
                        drop.shrink(1);
                    }

                    if (!drop.isEmpty()) {
                        Block.popResource(level, pos, drop);
                    }
                }

                level.globalLevelEvent(2001, pos, Block.getId(newState));
                level.setBlockAndUpdate(pos, newState);
                level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

                triggerReplantedCrops(serverPlayer);

                // damage the hoe a bit
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                return InteractionResult.CONSUME;
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public static void tryAddReplantable(String blockState) {
        BlockState state;

        try {
            BlockStateParser parser = new BlockStateParser(new StringReader(blockState), false).parse(false);
            state = parser.getState();
        } catch (Exception e) {
            state = null;
        }

        if (state != null) {
            REPLANTABLE.add(state);
        }
    }

    public static void triggerReplantedCrops(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_REPLANTED_CROPS);
    }
}
