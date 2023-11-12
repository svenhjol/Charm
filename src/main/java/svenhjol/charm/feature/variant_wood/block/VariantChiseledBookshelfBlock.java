package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.SoundType;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.iface.IFuelProvider;
import svenhjol.charmony_api.iface.IVariantMaterial;

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

    public static class BlockItem extends CharmonyBlockItem implements IFuelProvider {
        IVariantMaterial material;

        public BlockItem(Supplier<VariantChiseledBookshelfBlock> block) {
            super(block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
