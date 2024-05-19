package svenhjol.charm.feature.arcane_purpur.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Block> CHORUS_TELEPORTS = TagKey.create(Registries.BLOCK,
        Charm.id("chorus_teleports"));
}
