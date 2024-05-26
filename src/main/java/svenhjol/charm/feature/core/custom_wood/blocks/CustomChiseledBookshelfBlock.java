package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.level.block.SoundType;
import svenhjol.charm.api.iface.FuelProvider;
import svenhjol.charm.api.iface.CustomMaterial;

import java.util.function.Supplier;

public class CustomChiseledBookshelfBlock extends net.minecraft.world.level.block.ChiseledBookShelfBlock {
    private final CustomMaterial material;

    public CustomChiseledBookshelfBlock(CustomMaterial material) {
        super(material.blockProperties()
            .strength(1.5F)
            .sound(SoundType.CHISELED_BOOKSHELF));

        this.material = material;
    }

    public CustomMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements FuelProvider {
        CustomMaterial material;

        public BlockItem(Supplier<CustomChiseledBookshelfBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
