package svenhjol.charm.module.variant_chests;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.enums.IVariantMaterial;

import javax.annotation.Nullable;

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
        if (materialType == null && world != null)
            return ((IVariantChestBlock)this.getCachedState().getBlock()).getMaterialType();

        return materialType;
    }

    public void setMaterialType(IVariantMaterial materialType) {
        this.materialType = materialType;
    }
}
