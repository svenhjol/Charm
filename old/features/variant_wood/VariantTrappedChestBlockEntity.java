package svenhjol.charm.feature.variant_wood;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VariantTrappedChestBlockEntity extends VariantChestBlockEntity {
    public VariantTrappedChestBlockEntity(BlockPos pos, BlockState state) {
        this(CustomTrappedChest.blockEntity.get(), pos, state);
    }

    public VariantTrappedChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
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
