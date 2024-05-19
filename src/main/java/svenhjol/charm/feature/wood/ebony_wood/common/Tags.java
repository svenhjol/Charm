package svenhjol.charm.feature.wood.ebony_wood.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Biome> GROWS_EBONY_TREES = TagKey.create(Registries.BIOME,
        Charm.id("grows_ebony_trees"));
}
