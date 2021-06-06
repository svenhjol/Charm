package svenhjol.charm.module.dirt_to_path;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;

@Module(mod = Charm.MOD_ID, description = "Right-clicking dirt with a shovel turns it into grass path.")
public class DirtToPath extends CharmModule {
    public static final ResourceLocation TRIGGER_CONVERTED_DIRT = new ResourceLocation(Charm.MOD_ID, "converted_dirt");

    @Override
    public void init() {
        UseBlockCallback.EVENT.register(this::convertDirt);
    }

    private InteractionResult convertDirt(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        ItemStack stack = player.getItemInHand(hand);

        if (world != null && stack.getItem() instanceof ShovelItem) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.DIRT) {
                player.swing(hand);

                if (!world.isClientSide) {
                    world.setBlock(pos, Blocks.DIRT_PATH.defaultBlockState(), 11);
                    world.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);

                    triggerConvertedDirt((ServerPlayer) player);

                    // damage the shovel a bit
                    stack.hurtAndBreak(1, player, p -> p.swing(hand));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static void triggerConvertedDirt(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_CONVERTED_DIRT);
    }
}
