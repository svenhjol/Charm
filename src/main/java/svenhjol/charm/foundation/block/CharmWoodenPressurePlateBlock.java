package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

import java.util.function.Supplier;

public class CharmWoodenPressurePlateBlock extends PressurePlateBlock {
    protected final IVariantMaterial variantMaterial;

    public CharmWoodenPressurePlateBlock(IVariantWoodMaterial material) {
        super(material.blockSetType(), material.blockProperties()
            .strength(0.5F)
            .noCollission());

        this.variantMaterial = material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
