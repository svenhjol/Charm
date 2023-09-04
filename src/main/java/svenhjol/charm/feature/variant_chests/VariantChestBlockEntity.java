package svenhjol.charm.feature.variant_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.api.iface.IVariantMaterial;

import javax.annotation.Nullable;

public class VariantChestBlockEntity extends ChestBlockEntity {
    @Nullable
    private IVariantMaterial material = null;

    public VariantChestBlockEntity(BlockPos pos, BlockState state) {
        this(VariantChests.NORMAL_BLOCK_ENTITY.get(), pos, state);
    }

    public VariantChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    @Nullable
    public IVariantMaterial getMaterial() {
        if (material == null && level != null) {
            return ((IVariantChest) this.getBlockState().getBlock()).getMaterial();
        }

        return material;
    }

    public void setMaterial(IVariantMaterial material) {
        this.material = material;
    }
}
