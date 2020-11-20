package svenhjol.charm.base;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import svenhjol.charm.mixin.accessor.BlockTagsAccessor;

public class CharmTags {
    public static Tag.Identified<Block> BARRELS;
    public static Tag.Identified<Block> IMMOVABLE_BY_PISTONS;

    public static void init() {
        BARRELS = BlockTagsAccessor.invokeRegister("charm:barrels");
        IMMOVABLE_BY_PISTONS = BlockTagsAccessor.invokeRegister("charm:immovable_by_pistons");
    }
}
