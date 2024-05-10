package svenhjol.charm.feature.variant_wood.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.foundation.Resolve;

public class TrappedChestBlockEntity extends ChestBlockEntity {
    private static final VariantWood FEATURE = Resolve.feature(VariantWood.class);
    public TrappedChestBlockEntity(BlockPos pos, BlockState state) {
        this(FEATURE.registers.trappedChestBlockEntity.get(), pos, state);
    }

    public TrappedChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
        super.signalOpenCount(level, blockPos, blockState, i, j);
        if (i != j) {
            Block block = blockState.getBlock();
            level.updateNeighborsAt(blockPos, block);
            level.updateNeighborsAt(blockPos.below(), block);
        }
    }
}
