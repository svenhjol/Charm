package svenhjol.charm.module.variant_chests;

import svenhjol.charm.enums.IVariantMaterial;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.module.variant_chests.IVariantChestBlock;
import svenhjol.charm.module.variant_chests.VariantChests;

public class VariantChestBlockEntity extends ChestBlockEntity {
    private IVariantMaterial materialType = null;

    public VariantChestBlockEntity(BlockPos pos, BlockState state) {
        super(VariantChests.NORMAL_BLOCK_ENTITY, pos, state);
    }

    public VariantChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    @Nullable
    public IVariantMaterial getMaterialType() {
        if (materialType == null && level != null)
            return ((IVariantChestBlock)this.getBlockState().getBlock()).getMaterialType();

        return materialType;
    }

    public void setMaterialType(IVariantMaterial materialType) {
        this.materialType = materialType;
    }
}
