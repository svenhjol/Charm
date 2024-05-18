package svenhjol.charm.feature.extra_portal_frames.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

public class Tags {
    public static final TagKey<Block> NETHER_PORTAL_FRAMES = TagKey.create(Registries.BLOCK,
        Charm.id("nether_portal_frames"));
}
