package svenhjol.charm.feature.mooblooms.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Biome> SPAWNS_CHERRY_BLOSSOM_MOOBLOOMS = TagKey.create(Registries.BIOME,
        Charm.id("spawns_cherry_blossom_mooblooms"));
    public static final TagKey<Biome> SPAWNS_COMMON_MOOBLOOMS = TagKey.create(Registries.BIOME,
        Charm.id("spawns_common_mooblooms"));
    public static final TagKey<Biome> SPAWNS_SUNFLOWER_MOOBLOOMS = TagKey.create(Registries.BIOME,
        Charm.id("spawns_sunflower_mooblooms"));
}
