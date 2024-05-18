package svenhjol.charm.feature.coral_squids.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import svenhjol.charm.Charm;

public final class Tags {
    public final static TagKey<Biome> SPAWNS_CORAL_SQUIDS = TagKey.create(Registries.BIOME,
        Charm.id("spawns_coral_squids"));
}
