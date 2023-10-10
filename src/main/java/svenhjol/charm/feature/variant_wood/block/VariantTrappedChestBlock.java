package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.entity.VariantTrappedChestBlockEntity;
import svenhjol.charm.feature.variant_wood.iface.IVariantChest;
import svenhjol.charm.feature.variant_wood.registry.CustomTrappedChest;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.iface.IFuelProvider;
import svenhjol.charmony_api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class VariantTrappedChestBlock extends ChestBlock implements IVariantChest {
    private final IVariantMaterial material;

    public VariantTrappedChestBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.TRAPPED_CHEST), () -> CustomTrappedChest.blockEntity.get());
        this.material = material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VariantTrappedChestBlockEntity(pos, state);
    }

    @Override
    public IVariantMaterial getMaterial() {
        return material;
    }

    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    /**
     * Vanilla flagged as deprecated
     */
    @SuppressWarnings("deprecation")
    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    /**
     * Vanilla flagged as deprecated
     */
    @SuppressWarnings("deprecation")
    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        var c = Mth.clamp(VariantTrappedChestBlockEntity.getOpenCount(blockGetter, blockPos), 0, 15);
        return c;
    }

    /**
     * Vanilla flagged as deprecated
     */
    @SuppressWarnings("deprecation")
    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        if (direction == Direction.UP) {
            return blockState.getSignal(blockGetter, blockPos, direction);
        }
        return 0;
    }

    static VariantWood getParent() {
        return Charm.instance().loader().get(VariantWood.class).orElseThrow();
    }

    public static class BlockItem extends CharmonyBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantTrappedChestBlock> block) {
            super(getParent(), block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && VariantWood.variantChests;
        }
    }
}
