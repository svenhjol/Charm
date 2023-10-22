package svenhjol.charm;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CharmTags {
    static final ResourceKey<? extends Registry<Item>> ITEM_KEY = BuiltInRegistries.ITEM.key();
    static final ResourceKey<? extends Registry<Block>> BLOCK_KEY = BuiltInRegistries.BLOCK.key();
    static final ResourceKey<? extends Registry<EntityType<?>>> ENTITY_TYPE_KEY = BuiltInRegistries.ENTITY_TYPE.key();

    public static final TagKey<Block> BARRELS = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "barrels"));

    public static final TagKey<Block> CHISELED_BOOKSHELVES = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "chiseled_bookshelves"));

    public static final TagKey<Block> CHORUS_TELEPORTS = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "chorus_teleports"));

    public static final TagKey<Item> COLORED_DYES = TagKey.create(ITEM_KEY,
        new ResourceLocation(Charm.ID, "colored_dyes"));

    public static final TagKey<Item> HAS_SUSPICIOUS_EFFECTS = TagKey.create(ITEM_KEY,
        new ResourceLocation(Charm.ID, "has_suspicious_effects"));

    public static final TagKey<Block> LADDERS = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "ladders"));

    public static final TagKey<Block> OVERWORLD_STRIPPED_LOGS = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "overworld_stripped_logs"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_BLAZE_RODS = TagKey.create(ENTITY_TYPE_KEY,
        new ResourceLocation(Charm.ID, "spawners/drops_blaze_rods"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_BONES = TagKey.create(ENTITY_TYPE_KEY,
        new ResourceLocation(Charm.ID, "spawners/drops_bones"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_GUNPOWDER = TagKey.create(ENTITY_TYPE_KEY,
        new ResourceLocation(Charm.ID, "spawners/drops_gunpowder"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_MAGMA_CREAM = TagKey.create(ENTITY_TYPE_KEY,
        new ResourceLocation(Charm.ID, "spawners/drops_magma_cream"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_ROTTEN_FLESH = TagKey.create(ENTITY_TYPE_KEY,
        new ResourceLocation(Charm.ID, "spawners/drops_rotten_flesh"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_SLIME_BALLS = TagKey.create(ENTITY_TYPE_KEY,
        new ResourceLocation(Charm.ID, "spawners/drops_slime_balls"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_SPIDER_EYES = TagKey.create(ENTITY_TYPE_KEY,
        new ResourceLocation(Charm.ID, "spawners/drops_spider_eyes"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_STRING = TagKey.create(ENTITY_TYPE_KEY,
        new ResourceLocation(Charm.ID, "spawners/drops_string"));

    public static final TagKey<Block> PISTONS = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "pistons"));

    public static final TagKey<Block> STICKY_PISTONS = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "sticky_pistons"));

    public static final TagKey<Block> PISTON_HEADS = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "piston_heads"));

    public static final TagKey<Block> MOVING_PISTONS = TagKey.create(BLOCK_KEY,
        new ResourceLocation(Charm.ID, "moving_pistons"));
}