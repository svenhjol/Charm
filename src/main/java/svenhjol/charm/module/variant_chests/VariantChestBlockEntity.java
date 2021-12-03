package svenhjol.charm.module.variant_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.enums.IWoodMaterial;

import javax.annotation.Nullable;

public class VariantChestBlockEntity extends ChestBlockEntity {
    private IWoodMaterial materialType = null;

    public VariantChestBlockEntity(BlockPos pos, BlockState state) {
        super(VariantChests.NORMAL_BLOCK_ENTITY, pos, state);
    }

    public VariantChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    @Nullable
    public IWoodMaterial getMaterialType() {
        if (materialType == null && level != null) {
            return ((IVariantChestBlock) this.getBlockState().getBlock()).getMaterialType();
        }

        return materialType;
    }

    public void setMaterialType(IWoodMaterial materialType) {
        this.materialType = materialType;
    }
}
