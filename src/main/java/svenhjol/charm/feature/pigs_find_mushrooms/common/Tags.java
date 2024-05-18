package svenhjol.charm.feature.pigs_find_mushrooms.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Block> PIGS_FIND_MUSHROOMS = TagKey.create(Registries.BLOCK,
        Charm.id("pigs_find_mushrooms"));
}
