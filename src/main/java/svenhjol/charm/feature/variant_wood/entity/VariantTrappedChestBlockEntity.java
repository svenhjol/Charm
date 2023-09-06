package svenhjol.charm.feature.variant_wood.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.variant_wood.registry.CustomTrappedChest;

public class VariantTrappedChestBlockEntity extends VariantChestBlockEntity {
    public VariantTrappedChestBlockEntity(BlockPos pos, BlockState state) {
        this(CustomTrappedChest.blockEntity.get(), pos, state);
    }

    public VariantTrappedChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }
}
