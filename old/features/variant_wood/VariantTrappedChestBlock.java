package svenhjol.charm.feature.variant_wood;

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
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class VariantTrappedChestBlock extends ChestBlock implements VariantChest {
    private final IVariantMaterial material;

    public VariantTrappedChestBlock(IVariantMaterial material) {
        super(Properties.ofFullCopy(Blocks.TRAPPED_CHEST), () -> CustomTrappedChest.blockEntity.get());
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
        return Mth.clamp(VariantTrappedChestBlockEntity.getOpenCount(blockGetter, blockPos), 0, 15);
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

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantTrappedChestBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}