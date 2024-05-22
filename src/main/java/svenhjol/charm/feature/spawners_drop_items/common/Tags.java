package svenhjol.charm.feature.spawners_drop_items.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_BLAZE_RODS = TagKey.create(Registries.ENTITY_TYPE,
        Charm.id("spawners/drops_blaze_rods"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_BONES = TagKey.create(Registries.ENTITY_TYPE,
        Charm.id("spawners/drops_bones"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_GUNPOWDER = TagKey.create(Registries.ENTITY_TYPE,
        Charm.id("spawners/drops_gunpowder"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_MAGMA_CREAM = TagKey.create(Registries.ENTITY_TYPE,
        Charm.id("spawners/drops_magma_cream"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_ROTTEN_FLESH = TagKey.create(Registries.ENTITY_TYPE,
        Charm.id("spawners/drops_rotten_flesh"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_SLIME_BALLS = TagKey.create(Registries.ENTITY_TYPE,
        Charm.id("spawners/drops_slime_balls"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_SPIDER_EYES = TagKey.create(Registries.ENTITY_TYPE,
        Charm.id("spawners/drops_spider_eyes"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_STRING = TagKey.create(Registries.ENTITY_TYPE,
        Charm.id("spawners/drops_string"));
}
