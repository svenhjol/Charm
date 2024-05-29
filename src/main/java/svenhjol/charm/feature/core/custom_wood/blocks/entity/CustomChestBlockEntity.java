package svenhjol.charm.feature.core.custom_wood.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.CustomMaterial;
import svenhjol.charm.api.iface.VariantMaterialHolder;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.charmony.Resolve;

import javax.annotation.Nullable;

public class CustomChestBlockEntity extends ChestBlockEntity {
    @Nullable
    private CustomMaterial material = null;

    public CustomChestBlockEntity(BlockPos pos, BlockState state) {
        this(Resolve.feature(CustomWood.class).registers.chestBlockEntity.get(), pos, state);
    }

    public CustomChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    @Nullable
    public CustomMaterial getMaterial() {
        if (material == null && level != null && getBlockState().getBlock() instanceof VariantMaterialHolder holder) {
            return holder.getMaterial();
        }

        return material;
    }

    public void setMaterial(CustomMaterial material) {
        this.material = material;
    }
}
