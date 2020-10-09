package svenhjol.charm.base;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import svenhjol.meson.mixin.accessor.BlockTagsAccessor;

public class CharmTags {
    public static Tag.Identified<Block> BARRELS;

    public static void init() {
        BARRELS = BlockTagsAccessor.invokeRegister("charm:barrels");
    }
}
