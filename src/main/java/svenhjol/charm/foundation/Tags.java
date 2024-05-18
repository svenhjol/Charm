package svenhjol.charm.foundation;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

@Deprecated
@SuppressWarnings("unused")
public final class Tags {
    public static final TagKey<Item> HAS_SUSPICIOUS_EFFECTS = TagKey.create(Registries.ITEM,
        Charm.id("has_suspicious_effects"));

    public static final TagKey<Block> NEARBY_WORKSTATIONS = TagKey.create(Registries.BLOCK,
        Charm.id("nearby_workstations"));

    public static final TagKey<Block> PIGS_FIND_MUSHROOMS = TagKey.create(Registries.BLOCK,
        Charm.id("pigs_find_mushrooms"));

    public static final TagKey<Item> REPAIRABLE_USING_SCRAP = TagKey.create(Registries.ITEM,
        Charm.id("repairable_using_scrap"));

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
