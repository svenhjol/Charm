package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ButtonBlock;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;

import java.util.function.Supplier;

public class CustomWoodenButtonBlock extends ButtonBlock {
    public CustomWoodenButtonBlock(CustomWoodMaterial material) {
        super(material.blockProperties().strength(0.5F),
                material.blockSetType(),
                30, true);
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<CustomWoodenButtonBlock> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
