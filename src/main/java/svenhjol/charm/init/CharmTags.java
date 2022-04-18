package svenhjol.charm.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class CharmTags {
    public static TagKey<Block> NETHER_PORTAL_FRAMES;
    public static TagKey<Block> PROVIDES_ENCHANTING_POWER;
    public static TagKey<Block> IRON;
    public static TagKey<Item> CHESTS;
    public static TagKey<Item> CRAFTING_TABLES;
    public static TagKey<Structure> ENDERMITE_POWDER_LOCATED;

    public static void init() {
        PROVIDES_ENCHANTING_POWER = BlockTags.create("c:provides_enchanting_power");
        CRAFTING_TABLES = ItemTags.bind("charm:crafting_tables");
        NETHER_PORTAL_FRAMES = BlockTags.create("charm:nether_portal_frames");
        ENDERMITE_POWDER_LOCATED = StructureTags.create("charm:endermite_powder_located");
        CHESTS = ItemTags.bind("charm:chests/normal");
        IRON = BlockTags.create("charm:iron");
    }
}
