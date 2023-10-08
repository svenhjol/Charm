package svenhjol.charm.feature.quick_replant;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony.helper.CharmonyEnchantmentHelper;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.BlockUseEvent;
import svenhjol.charmony_api.iface.IQuickReplantProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
@Feature(mod = Charm.MOD_ID, description = "Right-click with a hoe to quickly harvest and replant a fully-grown crop.")
public class QuickReplant extends CharmonyFeature implements IQuickReplantProvider {
    static final List<BlockState> REPLANTABLE = new ArrayList<>();
    static final List<Block> NOT_REPLANTABLE = List.of(
        Blocks.TORCHFLOWER,
        Blocks.PITCHER_CROP,
        Blocks.PITCHER_PLANT
    );

    @Override
    public void register() {
        ApiHelper.consume(IQuickReplantProvider.class,
            provider -> provider.getHarvestableBlocks().forEach(
                supplier -> REPLANTABLE.add(supplier.get())));

        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        BlockUseEvent.INSTANCE.handle(this::handleBlockUse);
    }

    private InteractionResult handleBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var mainhand = player.getMainHandItem();
        var offhand = player.getOffhandItem();
        ItemStack held;

        if (mainhand.getItem() instanceof HoeItem) {
            held = mainhand;
        } else if (offhand.getItem() instanceof HoeItem) {
            held = offhand;
        } else {
            return InteractionResult.PASS;
        }

        var pos = hitResult.getBlockPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();
        var doReplant = !NOT_REPLANTABLE.contains(block);

        if (!REPLANTABLE.contains(state)) {
            return InteractionResult.PASS;
        }

        var blockItem = block.asItem();
        var newState = block.defaultBlockState();

        // Preserve FACING property of original state.
        for (Property<?> prop : state.getProperties()) {
            if (prop.getName().toLowerCase(Locale.ENGLISH).equals("facing")) {
                newState = newState.setValue((Property)prop, state.getValue(prop));
                break;
            }
        }

        if (!level.isClientSide) {
            var serverPlayer = (ServerPlayer)player;
            var serverLevel = (ServerLevel)serverPlayer.level();
            var drops = Block.getDrops(state, serverLevel, pos, null, player, ItemStack.EMPTY);
            var hasCollection = Charm.instance().loader().isEnabled("Collection")
                && CharmonyEnchantmentHelper.itemHasEnchantment(held, Charm.instance().makeId("collection"));

            for (var drop : drops) {
                if (doReplant && drop.getItem() == blockItem ) {
                    drop.shrink(1);
                }

                if (!drop.isEmpty()) {
                    if (hasCollection) {
                        player.getInventory().placeItemBackInInventory(drop);
                    } else {
                        Block.popResource(level, pos, drop);
                    }
                }
            }

            // If this crop should not be replanted, just set the new state to air.
            if (!doReplant) {
                newState = Blocks.AIR.defaultBlockState();
            }

            level.globalLevelEvent(2001, pos, Block.getId(newState));
            level.setBlockAndUpdate(pos, newState);
            level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

            triggerReplantedCrops(serverPlayer);

            // Damage the hoe a bit.
            if (!player.getAbilities().instabuild) {
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }

            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public List<Supplier<BlockState>> getHarvestableBlocks() {
        List<Supplier<BlockState>> harvestables = new ArrayList<>(List.of(
            () -> Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, 3),
            () -> Blocks.CARROTS.defaultBlockState().setValue(CarrotBlock.AGE, 7),
            () -> Blocks.NETHER_WART.defaultBlockState().setValue(NetherWartBlock.AGE, 3),
            () -> Blocks.POTATOES.defaultBlockState().setValue(PotatoBlock.AGE, 7),
            () -> Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 7),
            () -> Blocks.PITCHER_CROP.defaultBlockState().setValue(PitcherCropBlock.AGE, 4),
            () -> Blocks.PITCHER_PLANT.defaultBlockState(),
            () -> Blocks.TORCHFLOWER.defaultBlockState()
        ));

        // Cocoa also has FACING property. We need to capture all states with AGE=2.
        Blocks.COCOA.getStateDefinition().getPossibleStates().stream()
            .filter(s -> s.getValue(CocoaBlock.AGE) == 2)
            .forEach(s -> harvestables.add(() -> s));

        return harvestables;
    }

    public static void triggerReplantedCrops(Player player) {
        Advancements.trigger(Charm.instance().makeId("replanted_crops"), player);
    }
}
