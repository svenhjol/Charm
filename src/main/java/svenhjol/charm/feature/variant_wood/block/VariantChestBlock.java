package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.entity.VariantChestBlockEntity;
import svenhjol.charm.feature.variant_wood.iface.IVariantChest;
import svenhjol.charm.feature.variant_wood.registry.CustomChest;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.iface.IFuelProvider;

import java.util.function.Supplier;

public class VariantChestBlock extends ChestBlock implements IVariantChest {
    private final IVariantMaterial material;

    public VariantChestBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.CHEST), () -> CustomChest.blockEntity.get());
        this.material = material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VariantChestBlockEntity(pos, state);
    }

    @Override
    public IVariantMaterial getMaterial() {
        return material;
    }

    static VariantWood getParent() {
        return Charm.instance().loader().get(VariantWood.class).orElseThrow();
    }

    public static class BlockItem extends CharmBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantChestBlock> block) {
            super(getParent(), block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && VariantWood.variantChests;
        }
    }
}
