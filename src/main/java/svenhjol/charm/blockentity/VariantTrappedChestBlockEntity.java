package svenhjol.charm.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.module.VariantChests;

public class VariantTrappedChestBlockEntity extends VariantChestBlockEntity {
    public VariantTrappedChestBlockEntity(BlockPos pos, BlockState state) {
        super(VariantChests.TRAPPED_BLOCK_ENTITY, pos, state);
    }
}
