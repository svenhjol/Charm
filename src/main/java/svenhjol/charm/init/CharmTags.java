package svenhjol.charm.init;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.mixin.accessor.BlockTagsAccessor;
import svenhjol.charm.mixin.accessor.ItemTagsAccessor;

public class CharmTags {
    public static Tag.Named<Block> BARRELS;
    public static Tag.Named<Block> IMMOVABLE_BY_PISTONS;
    public static Tag.Named<Block> NETHER_PORTAL_FRAMES;
    public static Tag.Named<Block> PROVIDE_ENCHANTING_POWER;
    public static Tag.Named<Item> CRAFTING_TABLES;

    public static void init() {
        BARRELS = BlockTagsAccessor.invokeBind("charm:barrels");
        IMMOVABLE_BY_PISTONS = BlockTagsAccessor.invokeBind("charm:immovable_by_pistons");
        NETHER_PORTAL_FRAMES = BlockTagsAccessor.invokeBind("charm:nether_portal_frames");
        PROVIDE_ENCHANTING_POWER = BlockTagsAccessor.invokeBind("c:provides_enchanting_power");
        CRAFTING_TABLES = ItemTagsAccessor.invokeBind("charm:crafting_tables");
    }
}
