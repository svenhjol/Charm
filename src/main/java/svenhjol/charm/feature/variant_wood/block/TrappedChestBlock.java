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
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.common.ChestMaterial;
import svenhjol.charm.feature.variant_wood.block.entity.TrappedChestBlockEntity;
import svenhjol.charm.foundation.Resolve;

import java.util.function.Supplier;

public class TrappedChestBlock extends ChestBlock implements ChestMaterial {
    private static final VariantWood VARIANT_WOOD = Resolve.feature(VariantWood.class);
    private final IVariantMaterial material;

    public TrappedChestBlock(IVariantMaterial material) {
        super(Properties.ofFullCopy(Blocks.TRAPPED_CHEST), VARIANT_WOOD.registers.trappedChestBlockEntity::get);
        this.material = material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TrappedChestBlockEntity(pos, state);
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
    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    /**
     * Vanilla flagged as deprecated
     */
    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return Mth.clamp(TrappedChestBlockEntity.getOpenCount(blockGetter, blockPos), 0, 15);
    }

    /**
     * Vanilla flagged as deprecated
     */
    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        if (direction == Direction.UP) {
            return blockState.getSignal(blockGetter, blockPos, direction);
        }
        return 0;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<TrappedChestBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}