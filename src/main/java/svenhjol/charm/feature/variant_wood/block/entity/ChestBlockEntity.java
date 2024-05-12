package svenhjol.charm.feature.variant_wood.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.common.ChestMaterial;
import svenhjol.charm.foundation.Resolve;

import javax.annotation.Nullable;

public class ChestBlockEntity extends net.minecraft.world.level.block.entity.ChestBlockEntity {
    private static final VariantWood VARIANT_WOOD = Resolve.feature(VariantWood.class);

    @Nullable
    private IVariantMaterial material = null;

    public ChestBlockEntity(BlockPos pos, BlockState state) {
        this(VARIANT_WOOD.registers.chestBlockEntity.get(), pos, state);
    }

    public ChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    @Nullable
    public IVariantMaterial getMaterial() {
        if (material == null && level != null) {
            return ((ChestMaterial) this.getBlockState().getBlock()).getMaterial();
        }

        return material;
    }

    public void setMaterial(IVariantMaterial material) {
        this.material = material;
    }
}
