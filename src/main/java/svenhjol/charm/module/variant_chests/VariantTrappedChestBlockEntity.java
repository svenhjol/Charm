package svenhjol.charm.module.variant_chests;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class VariantTrappedChestBlockEntity extends VariantChestBlockEntity {
    public VariantTrappedChestBlockEntity(BlockPos pos, BlockState state) {
        this(VariantChests.TRAPPED_BLOCK_ENTITY, pos, state);
    }

    public VariantTrappedChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }
}
