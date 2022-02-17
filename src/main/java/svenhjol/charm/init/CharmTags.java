package svenhjol.charm.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CharmTags {
    public static TagKey<Block> NETHER_PORTAL_FRAMES;
    public static TagKey<Block> PROVIDE_ENCHANTING_POWER;
    public static TagKey<Item> CRAFTING_TABLES;

    public static void init() {
        PROVIDE_ENCHANTING_POWER = BlockTags.create("c:provides_enchanting_power");
        CRAFTING_TABLES = ItemTags.bind("charm:crafting_tables");
        NETHER_PORTAL_FRAMES = BlockTags.create("charm:nether_portal_frames");
    }
}
