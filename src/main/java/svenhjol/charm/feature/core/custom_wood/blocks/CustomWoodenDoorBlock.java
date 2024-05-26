package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DoorBlock;
import svenhjol.charm.api.iface.CustomWoodMaterial;

import java.util.function.Supplier;

public class CustomWoodenDoorBlock extends DoorBlock {
    public CustomWoodenDoorBlock(CustomWoodMaterial material) {
        super(material.blockSetType(), material.blockProperties()
            .strength(3.0F)
            .noOcclusion());
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<CustomWoodenDoorBlock> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
