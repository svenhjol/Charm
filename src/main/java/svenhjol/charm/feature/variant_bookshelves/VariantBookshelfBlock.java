package svenhjol.charm.feature.variant_bookshelves;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.iface.IFuelProvider;
import svenhjol.charmony.iface.IIgniteProvider;

import java.util.function.Supplier;

public class VariantBookshelfBlock extends Block implements IIgniteProvider {
    private final CharmFeature feature;
    private final IVariantMaterial material;

    public VariantBookshelfBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.BOOKSHELF));
        this.feature = getParent();
        this.material = material;
    }

    public IVariantMaterial getMaterial() {
        return material;
    }

    private static VariantBookshelves getParent() {
        return Charm.instance().loader().get(VariantBookshelves.class).orElseThrow();
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
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
