package svenhjol.charm;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CharmTags {
    static final ResourceKey<? extends Registry<Item>> ITEM_KEY = BuiltInRegistries.ITEM.key();
    static final ResourceKey<? extends Registry<Block>> BLOCK_KEY = BuiltInRegistries.BLOCK.key();
    static final ResourceKey<? extends Registry<EntityType<?>>> ENTITY_TYPE_KEY = BuiltInRegistries.ENTITY_TYPE.key();

    public static final TagKey<Item> CHESTS = TagKey.create(ITEM_KEY,
        Charm.instance().makeId("chests/normal"));
    public static final TagKey<Item> COLORED_DYES = TagKey.create(ITEM_KEY,
        Charm.instance().makeId("colored_dyes"));
    public static final TagKey<Item> HAS_SUSPICIOUS_EFFECTS = TagKey.create(ITEM_KEY,
        Charm.instance().makeId("has_suspicious_effects"));
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_BLAZE_RODS = TagKey.create(ENTITY_TYPE_KEY,
        Charm.instance().makeId("spawners/drops_blaze_rods"));
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_BONES = TagKey.create(ENTITY_TYPE_KEY,
        Charm.instance().makeId("spawners/drops_bones"));
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_GUNPOWDER = TagKey.create(ENTITY_TYPE_KEY,
        Charm.instance().makeId("spawners/drops_gunpowder"));
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_MAGMA_CREAM = TagKey.create(ENTITY_TYPE_KEY,
        Charm.instance().makeId("spawners/drops_magma_cream"));
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_ROTTEN_FLESH = TagKey.create(ENTITY_TYPE_KEY,
        Charm.instance().makeId("spawners/drops_rotten_flesh"));
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_SLIME_BALLS = TagKey.create(ENTITY_TYPE_KEY,
        Charm.instance().makeId("spawners/drops_slime_balls"));
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_SPIDER_EYES = TagKey.create(ENTITY_TYPE_KEY,
        Charm.instance().makeId("spawners/drops_spider_eyes"));
    public static final TagKey<EntityType<?>> SPAWNER_DROPS_STRING = TagKey.create(ENTITY_TYPE_KEY,
        Charm.instance().makeId("spawners/drops_string"));
}
