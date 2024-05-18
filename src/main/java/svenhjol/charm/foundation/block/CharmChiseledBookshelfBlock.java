package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.SoundType;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.CustomMaterial;

import java.util.function.Supplier;

public class CharmChiseledBookshelfBlock extends net.minecraft.world.level.block.ChiseledBookShelfBlock {
    private final CustomMaterial material;

    public CharmChiseledBookshelfBlock(CustomMaterial material) {
        super(material.blockProperties()
            .strength(1.5F)
            .sound(SoundType.CHISELED_BOOKSHELF));

        this.material = material;
    }

    public CustomMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        CustomMaterial material;

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
