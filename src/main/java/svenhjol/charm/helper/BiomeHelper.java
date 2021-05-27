package svenhjol.charm.helper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.GenerationSettingsAccessor;
import svenhjol.charm.mixin.accessor.SpawnSettingsAccessor;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "unused", "deprecation"})
public class BiomeHelper {
    public static final boolean USE_FABRIC_BIOME_API = true;
    public static Map<Biome.Category, List<RegistryKey<Biome>>> BIOME_CATEGORY_MAP = new HashMap<>();

    public static Biome getBiome(ServerWorld world, BlockPos pos) {
        BiomeAccess biomeAccess = world.getBiomeAccess();
        return biomeAccess.getBiome(pos);
    }

    public static Biome getBiomeFromBiomeKey(RegistryKey<Biome> biomeKey) {
        return BuiltinRegistries.BIOME.get(biomeKey);
    }

    public static Optional<RegistryKey<Biome>> getBiomeKeyAtPosition(ServerWorld world, BlockPos pos) {
        return world.getBiomeKey(pos);
    }

    public static BlockPos locateBiome(RegistryKey<Biome> biomeKey, ServerWorld world, BlockPos pos) {
        Biome biome = world.getRegistryManager().get(Registry.BIOME_KEY).get(biomeKey);
        return locateBiome(biome, world, pos);
    }

    public static BlockPos locateBiome(Biome biome, ServerWorld world, BlockPos pos) {
        return world.locateBiome(biome, pos, 6400, 8);
    }

    public static void addFeatureToBiomeCategories(ConfiguredFeature<?, ?> feature, Biome.Category biomeCategory, GenerationStep.Feature generationStep) {
        List<RegistryKey<Biome>> biomeKeys = BIOME_CATEGORY_MAP.get(biomeCategory);
        biomeKeys.forEach(biomeKey -> BiomeHelper.addFeatureToBiome(feature, biomeKey, generationStep));
    }

    public static void addFeatureToBiome(ConfiguredFeature<?, ?> feature, RegistryKey<Biome> biomeKey, GenerationStep.Feature generationStep) {
        if (USE_FABRIC_BIOME_API) {
            RegistryKey<ConfiguredFeature<?, ?>> featureKey;
            Predicate<BiomeSelectionContext> biomeSelector;

            try {
                biomeSelector = BiomeSelectors.includeByKey(biomeKey);
                featureKey = BuiltInRegistryKeys.get(feature);
            } catch (Exception e) {
                Charm.LOG.error("Failed to add feature to biome.");
                return;
            }

            BiomeModifications.addFeature(biomeSelector, generationStep, featureKey);
        } else {
            // Charm's biome hack
            GenerationSettings settings = getBiomeFromBiomeKey(biomeKey).getGenerationSettings();
            makeGenerationSettingsMutable(settings);
            ((GenerationSettingsAccessor) settings).getFeatures().get(generationStep.ordinal()).add(() -> feature);
        }
    }

    public static void addStructureToBiomeCategories(ConfiguredStructureFeature<?, ?> structureFeature, Biome.Category biomeCategory) {
        List<RegistryKey<Biome>> biomeKeys = BIOME_CATEGORY_MAP.get(biomeCategory);
        biomeKeys.forEach(biomeKey -> BiomeHelper.addStructureToBiome(structureFeature, biomeKey));
    }

    public static void addStructureToBiome(ConfiguredStructureFeature<?, ?> structureFeature, RegistryKey<Biome> biomeKey) {
        if (USE_FABRIC_BIOME_API) {
            RegistryKey<ConfiguredStructureFeature<?, ?>> structureKey;
            Predicate<BiomeSelectionContext> biomeSelector;

            try {
                biomeSelector = BiomeSelectors.includeByKey(biomeKey);
                structureKey = BuiltInRegistryKeys.get(structureFeature);
                Charm.LOG.debug("Added structure " + structureKey.toString() + " to biome " + biomeKey.toString());
            } catch (Exception e) {
                Charm.LOG.error("Failed to add structure to biome. This may cause crashes when trying to locate the structure.");
                return;
            }

            BiomeModifications.addStructure(biomeSelector, structureKey);
        } else {
            // Charm's biome hack
            GenerationSettings settings = getBiomeFromBiomeKey(biomeKey).getGenerationSettings();
            makeGenerationSettingsMutable(settings);
            ((GenerationSettingsAccessor) settings).getStructureFeatures().add(() -> structureFeature);
        }
    }

    public static void addSpawnEntry(RegistryKey<Biome> biomeKey, SpawnGroup group, EntityType<?> entity, int weight, int minGroupSize, int maxGroupSize) {
        if (USE_FABRIC_BIOME_API) {
            try {
                Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(biomeKey);
                BiomeModifications.addSpawn(biomeSelector, group, entity, weight, minGroupSize, maxGroupSize);
            } catch (Exception e) {
                Charm.LOG.error("Failed to add entity to biome spawn. This may cause crashes when trying to spawn the entity.");
            }
        } else {
            // Charm's biome hack
            SpawnSettings spawnSettings = getBiomeFromBiomeKey(biomeKey).getSpawnSettings();
            makeSpawnSettingsMutable(spawnSettings);

            Map<SpawnGroup, Pool<SpawnSettings.SpawnEntry>> spawners = ((SpawnSettingsAccessor) spawnSettings).getSpawners();
            CollectionHelper.addPoolEntry(spawners.get(group), new SpawnSettings.SpawnEntry(entity, weight, minGroupSize, maxGroupSize));

            ((SpawnSettingsAccessor)spawnSettings).setSpawners(spawners);
        }
    }

    /**
     * Charm's biome gen settings mutability hack, don't use unless fabric's biome API is b0rk
     */
    private static void makeGenerationSettingsMutable(GenerationSettings settings) {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> features = ((GenerationSettingsAccessor) settings).getFeatures();
        if (features instanceof ImmutableList) {
            List<List<Supplier<ConfiguredFeature<?, ?>>>> collect = features.stream().map(ArrayList::new).collect(Collectors.toList());
            ((GenerationSettingsAccessor) settings).setFeatures(collect);
        }

        List<Supplier<ConfiguredStructureFeature<?, ?>>> structureFeatures = ((GenerationSettingsAccessor)settings).getStructureFeatures();
        if (structureFeatures instanceof ImmutableList)
            ((GenerationSettingsAccessor)settings).setStructureFeatures(new ArrayList<>(structureFeatures));
    }

    /**
     * Charm's biome spawn settings mutability hack, don't use unless fabric's biome API is b0rk
     */
    private static void makeSpawnSettingsMutable(SpawnSettings settings) {
        Map<SpawnGroup, Pool<SpawnSettings.SpawnEntry>> spawners = ((SpawnSettingsAccessor) settings).getSpawners();
        if (spawners instanceof ImmutableMap)
            ((SpawnSettingsAccessor)settings).setSpawners(new HashMap<>(spawners));

        Map<EntityType<?>, SpawnSettings.SpawnDensity> spawnCosts = ((SpawnSettingsAccessor) settings).getSpawnCosts();
        if (spawnCosts instanceof ImmutableMap)
            ((SpawnSettingsAccessor)settings).setSpawnCosts(new HashMap<>(spawnCosts));
    }
}
