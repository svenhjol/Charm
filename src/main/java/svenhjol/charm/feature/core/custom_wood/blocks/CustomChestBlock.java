package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.iface.CustomMaterial;
import svenhjol.charm.charmony.iface.FuelProvider;
import svenhjol.charm.charmony.iface.VariantMaterialHolder;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.feature.core.custom_wood.blocks.entity.CustomChestBlockEntity;

import java.util.function.Supplier;

public class CustomChestBlock extends ChestBlock implements VariantMaterialHolder {
    private static final CustomWood CUSTOM_WOOD = Resolve.feature(CustomWood.class);
    private final CustomMaterial material;

    public CustomChestBlock(CustomMaterial material) {
        super(Properties.ofFullCopy(Blocks.CHEST), CUSTOM_WOOD.registers.chestBlockEntity::get);
        this.material = material;
    }

    @Override
    public CustomMaterial getMaterial() {
        return material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CustomChestBlockEntity(pos, state);
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements FuelProvider {
        private final CustomMaterial material;

        public BlockItem(Supplier<CustomChestBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
