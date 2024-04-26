package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DoorBlock;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

import java.util.function.Supplier;

public class CharmWoodenDoorBlock extends DoorBlock {
    public CharmWoodenDoorBlock(IVariantWoodMaterial material) {
        super(material.blockSetType(), material.blockProperties()
            .strength(3.0F)
            .noOcclusion());
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<CharmWoodenDoorBlock> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
