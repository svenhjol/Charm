package svenhjol.charm.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CharmTags {
    public static Tag.Named<Block> BARRELS;
    public static Tag.Named<Block> IMMOVABLE_BY_PISTONS;
    public static Tag.Named<Block> NETHER_PORTAL_FRAMES;
    public static Tag.Named<Block> PROVIDE_ENCHANTING_POWER;
    public static Tag.Named<Item> CRAFTING_TABLES;

    public static void init() {
        BARRELS = BlockTags.bind("charm:barrels");
        IMMOVABLE_BY_PISTONS = BlockTags.bind("charm:immovable_by_pistons");
        NETHER_PORTAL_FRAMES = BlockTags.bind("charm:nether_portal_frames");
        PROVIDE_ENCHANTING_POWER = BlockTags.bind("c:provides_enchanting_power");
        CRAFTING_TABLES = ItemTags.bind("charm:crafting_tables");
    }
}
