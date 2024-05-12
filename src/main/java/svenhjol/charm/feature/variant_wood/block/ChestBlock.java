package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.block.entity.ChestBlockEntity;
import svenhjol.charm.feature.variant_wood.common.ChestMaterial;
import svenhjol.charm.foundation.Resolve;

import java.util.function.Supplier;

public class ChestBlock extends net.minecraft.world.level.block.ChestBlock implements ChestMaterial {
    private static final VariantWood VARIANT_WOOD = Resolve.feature(VariantWood.class);
    private final IVariantMaterial material;

    public ChestBlock(IVariantMaterial material) {
        super(Properties.ofFullCopy(Blocks.CHEST), VARIANT_WOOD.registers.chestBlockEntity::get);
        this.material = material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChestBlockEntity(pos, state);
    }

    @Override
    public IVariantMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<ChestBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
