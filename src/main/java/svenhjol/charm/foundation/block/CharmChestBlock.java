package svenhjol.charm.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.CustomMaterial;
import svenhjol.charm.api.iface.VariantMaterialHolder;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.feature.core.custom_wood.common.ChestBlockEntity;
import svenhjol.charm.foundation.Resolve;

import java.util.function.Supplier;

public class CharmChestBlock extends ChestBlock implements VariantMaterialHolder {
    private static final CustomWood CUSTOM_WOOD = Resolve.feature(CustomWood.class);
    private final CustomMaterial material;

    public CharmChestBlock(CustomMaterial material) {
        super(Properties.ofFullCopy(Blocks.CHEST), CUSTOM_WOOD.registers.chestBlockEntity::get);
        this.material = material;
    }

    @Override
    public CustomMaterial getMaterial() {
        return material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChestBlockEntity(pos, state);
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final CustomMaterial material;

        public BlockItem(Supplier<CharmChestBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
