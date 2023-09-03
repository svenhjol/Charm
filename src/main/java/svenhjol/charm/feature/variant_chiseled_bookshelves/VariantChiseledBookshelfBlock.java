package svenhjol.charm.feature.variant_chiseled_bookshelves;

import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.SoundType;
import svenhjol.charm.Charm;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.iface.IFuelProvider;

import java.util.function.Supplier;

public class VariantChiseledBookshelfBlock extends ChiseledBookShelfBlock {
    private final IVariantWoodMaterial variantMaterial;

    public VariantChiseledBookshelfBlock(IVariantWoodMaterial material) {
        super(material.blockProperties()
            .strength(1.5F)
            .sound(SoundType.CHISELED_BOOKSHELF));

        this.variantMaterial = material;
    }

    public IVariantWoodMaterial getMaterial() {
        return variantMaterial;
    }

    private static VariantChiseledBookshelves getParent() {
        return Charm.instance().loader().get(VariantChiseledBookshelves.class).orElseThrow();
    }

    public static class BlockItem extends CharmBlockItem implements IFuelProvider {
        IVariantMaterial material;

        public BlockItem(Supplier<VariantChiseledBookshelfBlock> block) {
            super(getParent(), block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
