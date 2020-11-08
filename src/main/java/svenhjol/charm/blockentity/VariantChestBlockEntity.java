package svenhjol.charm.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.block.IVariantChestBlock;
import svenhjol.charm.module.VariantChests;
import svenhjol.charm.base.enums.IVariantMaterial;

import javax.annotation.Nullable;

public class VariantChestBlockEntity extends ChestBlockEntity {
    private IVariantMaterial materialType = null;

    public VariantChestBlockEntity(BlockPos pos, BlockState state) {
        super(VariantChests.NORMAL_BLOCK_ENTITY, pos, state);
    }

    public VariantChestBlockEntity(BlockEntityType<?> tile, BlockPos pos, BlockState state) {
        super(tile, pos, state);
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
