package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.SoundType;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class CharmChiseledBookshelfBlock extends net.minecraft.world.level.block.ChiseledBookShelfBlock {
    private final IVariantMaterial material;

    public CharmChiseledBookshelfBlock(IVariantMaterial material) {
        super(material.blockProperties()
            .strength(1.5F)
            .sound(SoundType.CHISELED_BOOKSHELF));

        this.material = material;
    }

    public IVariantMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        IVariantMaterial material;

        public BlockItem(Supplier<CharmChiseledBookshelfBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
