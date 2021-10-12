package svenhjol.charm.helper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.impl.biome.modification.BiomeSelectionContextImpl;
import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import svenhjol.charm.module.core.Core;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @version 2.0.0-charm
 */
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class BiomeHelper {
    public static Map<Biome.BiomeCategory, List<ResourceKey<Biome>>> BIOME_CATEGORY_MAP = new HashMap<>();

    public static Biome getBiome(ServerLevel world, BlockPos pos) {
        BiomeManager biomeAccess = world.getBiomeManager();
        return biomeAccess.getBiome(pos);
    }

    @Nullable
    public static Biome getBiomeFromBiomeKey(ResourceKey<Biome> biomeKey) {
        return BuiltinRegistries.BIOME.getOptional(biomeKey).orElse(null);
    }

    @Nullable
    public static ResourceKey<Biome> getBiomeKeyFromBiome(Biome biome) {
        return BuiltinRegistries.BIOME.getResourceKey(biome).orElse(null);
    }

    public static Optional<ResourceKey<Biome>> getBiomeKeyAtPosition(ServerLevel world, BlockPos pos) {
        return world.getBiomeName(pos);
    }

    public static BlockPos locateBiome(ResourceKey<Biome> biomeKey, ServerLevel world, BlockPos pos) {
        Biome biome = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(biomeKey);
        return locateBiome(biome, world, pos);
    }

    public static BlockPos locateBiome(Biome biome, ServerLevel world, BlockPos pos) {
        return world.findNearestBiome(biome, pos, 6400, 8);
    }

    public static void addFeatureToBiomeCategories(ConfiguredFeature<?, ?> feature, Biome.BiomeCategory biomeCategory, GenerationStep.Decoration generationStep) {
        List<ResourceKey<Biome>> biomeKeys = BIOME_CATEGORY_MAP.get(biomeCategory);
        biomeKeys.forEach(biomeKey -> BiomeHelper.addFeatureToBiome(feature, biomeKey, generationStep));
    }

    public static void addFeatureToBiome(ConfiguredFeature<?, ?> feature, ResourceKey<Biome> biomeKey, GenerationStep.Decoration generationStep) {
        ResourceKey<ConfiguredFeature<?, ?>> featureKey;
        Predicate<BiomeSelectionContext> biomeSelector;

        try {
            biomeSelector = BiomeSelectors.includeByKey(biomeKey);
            featureKey = BuiltInRegistryKeys.get(feature);
        } catch (Exception e) {
            LogHelper.error(BiomeHelper.class, "Failed to add feature to biome.");
            return;
        }

        BiomeModifications.addFeature(biomeSelector, generationStep, featureKey);
    }

    public static void addStructureToBiomeCategories(ConfiguredStructureFeature<?, ?> structureFeature, Biome.BiomeCategory biomeCategory) {
        List<ResourceKey<Biome>> biomeKeys = BIOME_CATEGORY_MAP.get(biomeCategory);
        biomeKeys.forEach(biomeKey -> BiomeHelper.addStructureToBiome(structureFeature, biomeKey));
    }

    public static void addStructureToBiome(ConfiguredStructureFeature<?, ?> structureFeature, ResourceKey<Biome> biomeKey) {
        ResourceKey<ConfiguredStructureFeature<?, ?>> structureKey;
        Predicate<BiomeSelectionContext> biomeSelector;

        try {
            biomeSelector = BiomeSelectors.includeByKey(biomeKey);
            structureKey = BuiltInRegistryKeys.get(structureFeature);
            LogHelper.debug(BiomeHelper.class, "Adding structure `" + structureFeature.feature.getFeatureName() + "` to biome `" + biomeKey.location() + "`");
        } catch (Exception e) {
            LogHelper.error(BiomeHelper.class, "Failed to add structure to biome. This may cause crashes when trying to locate the structure.");
            return;
        }

        BiomeModifications.addStructure(biomeSelector, structureKey);
    }

    public static void addSpawnEntry(ResourceKey<Biome> biomeKey, MobCategory group, EntityType<?> entity, int weight, int minGroupSize, int maxGroupSize) {
        try {
            Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(biomeKey);
            BiomeModifications.addSpawn(biomeSelector, group, entity, weight, minGroupSize, maxGroupSize);
        } catch (Exception e) {
            LogHelper.error(BiomeHelper.class, "Failed to add entity to biome spawn. This may cause crashes when trying to spawn the entity.");
        }
    }
}
