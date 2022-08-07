package svenhjol.charm.helper;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @version 4.0.0-charm
 */
@SuppressWarnings({"unused"})
public class BiomeHelper {
    public static Map<BiomeCategory, List<ResourceKey<Biome>>> BIOME_CATEGORY_MAP = new HashMap<>();

    public static Biome getBiome(ServerLevel level, BlockPos pos) {
        return getBiomeHolder(level, pos).value();
    }

    public static Holder<Biome> getBiomeHolder(ServerLevel level, BlockPos pos) {
        return level.getBiomeManager().getBiome(pos);
    }

    @Nullable
    public static Biome getBiomeFromBiomeKey(ResourceKey<Biome> biomeKey) {
        return BuiltinRegistries.BIOME.getOptional(biomeKey).orElse(null);
    }

    public static Holder<Biome> getBiomeHolderFromBiomeKey(ResourceKey<Biome> biomeKey) {
        return BuiltinRegistries.BIOME.getOrCreateHolder(biomeKey);
    }

    @Nullable
    public static ResourceKey<Biome> getBiomeKeyFromBiome(Biome biome) {
        return BuiltinRegistries.BIOME.getResourceKey(biome).orElse(null);
    }

    @Nullable
    public static BiomeCategory getBiomeCategoryByName(String name) {
        List<String> validCategories = Arrays.stream(BiomeCategory.values()).map(BiomeCategory::getSerializedName).toList();
        if (validCategories.contains(name)) {
            return BiomeCategory.byName(name);
        }
        return null;
    }

    @Nullable
    public static BlockPos locateBiome(ResourceLocation id, ServerLevel level, BlockPos pos) {
        Predicate<Holder<Biome>> biomeCheck = holder -> holder.is(id);
        return locateBiome(biomeCheck, level, pos);
    }

    public static BlockPos locateBiome(TagKey<Biome> tagKey, ServerLevel level, BlockPos pos) {
        Predicate<Holder<Biome>> biomeCheck = holder -> holder.is(tagKey);
        return locateBiome(biomeCheck, level, pos);
    }

    public static BlockPos locateBiome(Predicate<Holder<Biome>> biomeCheck, ServerLevel level, BlockPos pos) {
        var nearestBiome = level.findNearestBiome(biomeCheck, pos, 6400, 8);
        return nearestBiome != null ? nearestBiome.getFirst() : null;
    }

    public static void addSpawnEntry(ResourceKey<Biome> biomeKey, MobCategory group, EntityType<?> entity, int weight, int minGroupSize, int maxGroupSize) {
        Predicate<BiomeSelectionContext> context = c -> c.getBiomeKey() == biomeKey;
        BiomeModifications.addSpawn(context, group, entity, weight, minGroupSize, maxGroupSize);

        // This is broken, don't use it
//        MobSpawnSettings spawnSettings = getBiomeFromBiomeKey(biomeKey).getMobSettings();
//        makeSpawnSettingsMutable(spawnSettings);
//
//        Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners = spawnSettings.spawners;
//        CollectionHelper.addPoolEntry(spawners.get(group), new MobSpawnSettings.SpawnerData(entity, weight, minGroupSize, maxGroupSize));
//
//        spawnSettings.spawners = spawners;
    }

    /**
     * Charm's biome spawn settings mutability hack, don't use unless fabric's biome API is b0rk
     */
    private static void makeSpawnSettingsMutable(MobSpawnSettings settings) {
        Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners = settings.spawners;
        if (spawners instanceof ImmutableMap)
            settings.spawners = new HashMap<>(spawners);

        Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> spawnCosts = settings.mobSpawnCosts;
        if (spawnCosts instanceof ImmutableMap)
            settings.mobSpawnCosts = new HashMap<>(spawnCosts);
    }
}
