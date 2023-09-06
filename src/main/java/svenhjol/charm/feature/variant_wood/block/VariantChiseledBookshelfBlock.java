package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.SoundType;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.iface.IFuelProvider;

import java.util.function.Supplier;

public class VariantChiseledBookshelfBlock extends ChiseledBookShelfBlock {
    private final IVariantMaterial material;

    public VariantChiseledBookshelfBlock(IVariantMaterial material) {
        super(material.blockProperties()
            .strength(1.5F)
            .sound(SoundType.CHISELED_BOOKSHELF));

        this.material = material;
    }

    public IVariantMaterial getMaterial() {
        return material;
    }

    static VariantWood getParent() {
        return Charm.instance().loader().get(VariantWood.class).orElseThrow();
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

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && VariantWood.variantBookshelves;
        }
    }
}
