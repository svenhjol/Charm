package svenhjol.charm.feature.core.custom_wood.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.api.iface.VariantMaterialHolder;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.foundation.Resolve;

import javax.annotation.Nullable;

public class ChestBlockEntity extends net.minecraft.world.level.block.entity.ChestBlockEntity {
    private static final CustomWood CUSTOM_WOOD = Resolve.feature(CustomWood.class);

    @Nullable
    private IVariantMaterial material = null;

    public ChestBlockEntity(BlockPos pos, BlockState state) {
        this(CUSTOM_WOOD.registers.chestBlockEntity.get(), pos, state);
    }

    public ChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    @Nullable
    public IVariantMaterial getMaterial() {
        if (material == null && level != null && getBlockState().getBlock() instanceof VariantMaterialHolder holder) {
            return holder.getMaterial();
        }

        return material;
    }

    public void setMaterial(IVariantMaterial material) {
        this.material = material;
    }
}
