package svenhjol.charm;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class CharmTags {
    public static final TagKey<Block> BARRELS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "barrels"));

    public static final TagKey<Item> BEEKEEPER_SELLS_FLOWERS = TagKey.create(Registries.ITEM,
        new ResourceLocation(Charm.ID, "beekeeper_sells_flowers"));

    public static final TagKey<Block> CHISELED_BOOKSHELVES = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "chiseled_bookshelves"));

    public static final TagKey<Block> CHORUS_TELEPORTS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "chorus_teleports"));

    public static final TagKey<Item> COLORED_DYES = TagKey.create(Registries.ITEM,
        new ResourceLocation(Charm.ID, "colored_dyes"));

    public static final TagKey<Structure> ENDERMITE_POWDER_LOCATED = TagKey.create(Registries.STRUCTURE,
        new ResourceLocation(Charm.ID, "endermite_powder_located"));

    public static final TagKey<Item> HAS_SUSPICIOUS_EFFECTS = TagKey.create(Registries.ITEM,
        new ResourceLocation(Charm.ID, "has_suspicious_effects"));

    public static final TagKey<Block> LADDERS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "ladders"));

    public static final TagKey<Block> MOVING_PISTONS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "moving_pistons"));

    public static final TagKey<Block> NEARBY_WORKSTATIONS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "nearby_workstations"));

    public static final TagKey<Block> NETHER_PORTAL_FRAMES = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "nether_portal_frames"));

    public static final TagKey<Block> OVERWORLD_STRIPPED_LOGS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "overworld_stripped_logs"));

    public static final TagKey<Block> PIGS_FIND_MUSHROOMS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "pigs_find_mushrooms"));

    public static final TagKey<Block> PISTONS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "pistons"));

    public static final TagKey<Block> PISTON_HEADS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "piston_heads"));

    public static final TagKey<Item> REPAIRABLE_USING_SCRAP = TagKey.create(Registries.ITEM,
        new ResourceLocation(Charm.ID, "repairable_using_scrap"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_BLAZE_RODS = TagKey.create(Registries.ENTITY_TYPE,
        new ResourceLocation(Charm.ID, "spawners/drops_blaze_rods"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_BONES = TagKey.create(Registries.ENTITY_TYPE,
        new ResourceLocation(Charm.ID, "spawners/drops_bones"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_GUNPOWDER = TagKey.create(Registries.ENTITY_TYPE,
        new ResourceLocation(Charm.ID, "spawners/drops_gunpowder"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_MAGMA_CREAM = TagKey.create(Registries.ENTITY_TYPE,
        new ResourceLocation(Charm.ID, "spawners/drops_magma_cream"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_ROTTEN_FLESH = TagKey.create(Registries.ENTITY_TYPE,
        new ResourceLocation(Charm.ID, "spawners/drops_rotten_flesh"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_SLIME_BALLS = TagKey.create(Registries.ENTITY_TYPE,
        new ResourceLocation(Charm.ID, "spawners/drops_slime_balls"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_SPIDER_EYES = TagKey.create(Registries.ENTITY_TYPE,
        new ResourceLocation(Charm.ID, "spawners/drops_spider_eyes"));

    public static final TagKey<EntityType<?>> SPAWNER_DROPS_STRING = TagKey.create(Registries.ENTITY_TYPE,
        new ResourceLocation(Charm.ID, "spawners/drops_string"));

    public static final TagKey<Biome> SPAWNS_CHERRY_BLOSSOM_MOOBLOOMS = TagKey.create(Registries.BIOME,
        new ResourceLocation(Charm.ID, "spawns_cherry_blossom_mooblooms"));

    public static final TagKey<Biome> SPAWNS_COMMON_MOOBLOOMS = TagKey.create(Registries.BIOME,
        new ResourceLocation(Charm.ID, "spawns_common_mooblooms"));

    public final static TagKey<Biome> SPAWNS_CORAL_SQUIDS = TagKey.create(Registries.BIOME,
        new ResourceLocation(Charm.ID, "spawns_coral_squids"));

    public static final TagKey<Biome> SPAWNS_SUNFLOWER_MOOBLOOMS = TagKey.create(Registries.BIOME,
        new ResourceLocation(Charm.ID, "spawns_sunflower_mooblooms"));

    public static final TagKey<Block> STICKY_PISTONS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "sticky_pistons"));
}