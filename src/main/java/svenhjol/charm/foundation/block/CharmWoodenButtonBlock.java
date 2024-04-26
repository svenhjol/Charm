package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ButtonBlock;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

import java.util.function.Supplier;

public class CharmWoodenButtonBlock extends ButtonBlock {
    public CharmWoodenButtonBlock(IVariantWoodMaterial material) {
        super(material.blockSetType(),
            30,
            material.blockProperties()
                .strength(0.5F));
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<CharmWoodenButtonBlock> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
