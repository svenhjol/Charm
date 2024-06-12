package svenhjol.charm.feature.nether_portal_blocks.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

public class Tags {
    public static final TagKey<Block> NETHER_PORTAL_BLOCKS = TagKey.create(Registries.BLOCK,
        Charm.id("nether_portal_blocks"));
}
