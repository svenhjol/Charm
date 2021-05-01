package svenhjol.charm.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import svenhjol.charm.mixin.accessor.BlockTagsAccessor;
import svenhjol.charm.mixin.accessor.ItemTagsAccessor;

public class CharmTags {
    public static Tag.Identified<Block> BARRELS;
    public static Tag.Identified<Block> IMMOVABLE_BY_PISTONS;
    public static Tag.Identified<Block> NETHER_PORTAL_FRAMES;
    public static Tag.Identified<Block> PROVIDE_ENCHANTING_POWER;
    public static Tag.Identified<Item> CRAFTING_TABLES;

    public static void init() {
        BARRELS = BlockTagsAccessor.invokeRegister("charm:barrels");
        IMMOVABLE_BY_PISTONS = BlockTagsAccessor.invokeRegister("charm:immovable_by_pistons");
        NETHER_PORTAL_FRAMES = BlockTagsAccessor.invokeRegister("charm:nether_portal_frames");
        PROVIDE_ENCHANTING_POWER = BlockTagsAccessor.invokeRegister("charm:provide_enchanting_power");
        CRAFTING_TABLES = ItemTagsAccessor.invokeRegister("charm:crafting_tables");
    }
}
