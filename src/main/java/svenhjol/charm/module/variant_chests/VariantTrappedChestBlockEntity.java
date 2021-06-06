package svenhjol.charm.module.variant_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.module.variant_chests.VariantChestBlockEntity;
import svenhjol.charm.module.variant_chests.VariantChests;

public class VariantTrappedChestBlockEntity extends VariantChestBlockEntity {
    public VariantTrappedChestBlockEntity(BlockPos pos, BlockState state) {
        this(VariantChests.TRAPPED_BLOCK_ENTITY, pos, state);
    }

    public VariantTrappedChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }
}
