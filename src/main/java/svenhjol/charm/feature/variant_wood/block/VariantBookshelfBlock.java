package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.iface.IFuelProvider;
import svenhjol.charmony.iface.IIgniteProvider;

import java.util.function.Supplier;

public class VariantBookshelfBlock extends Block implements IIgniteProvider {
    private final IVariantMaterial material;

    public VariantBookshelfBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.BOOKSHELF));
        this.material = material;
    }

    public IVariantMaterial getMaterial() {
        return material;
    }

    static VariantWood getParent() {
        return Charm.instance().loader().get(VariantWood.class).orElseThrow();
    }

    @Override
    public int igniteChance() {
        return material.igniteChance();
    }

    @Override
    public int burnChance() {
        return material.burnChance();
    }

    public static class BlockItem extends CharmBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantBookshelfBlock> block) {
            super(getParent(), block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && VariantWood.variantBookshelves;
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
