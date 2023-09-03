package svenhjol.charm.feature.variant_bookshelves;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.IFuelProvider;
import svenhjol.charm_core.iface.IIgniteProvider;

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
