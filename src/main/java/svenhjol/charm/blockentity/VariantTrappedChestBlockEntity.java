package svenhjol.charm.blockentity;

import net.minecraft.block.entity.BlockEntityType;
import svenhjol.charm.module.VariantChests;

public class VariantTrappedChestBlockEntity extends VariantChestBlockEntity {
    public VariantTrappedChestBlockEntity() {
        super(VariantChests.TRAPPED_BLOCK_ENTITY);
    }

    public VariantTrappedChestBlockEntity(BlockEntityType<?> tile) {
        super(tile);
    }
}
