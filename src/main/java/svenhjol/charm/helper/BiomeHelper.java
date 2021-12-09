package svenhjol.charm.helper;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @version 4.0.0-charm
 */
@SuppressWarnings({"UnstableApiUsage", "unused"})
public class BiomeHelper {
    public static Map<BiomeCategory, List<ResourceKey<Biome>>> BIOME_CATEGORY_MAP = new HashMap<>();

    public static Biome getBiome(ServerLevel level, BlockPos pos) {
        BiomeManager biomeAccess = level.getBiomeManager();
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

    public static Optional<ResourceKey<Biome>> getBiomeKeyAtPosition(ServerLevel level, BlockPos pos) {
        return level.getBiomeName(pos);
    }

    @Nullable
    public static BiomeCategory getBiomeCategoryByName(String name) {
        List<String> validCategories = Arrays.stream(BiomeCategory.values()).map(BiomeCategory::getSerializedName).collect(Collectors.toList());
        if (validCategories.contains(name)) {
            return BiomeCategory.byName(name);
        }
        return null;
    }

    public static BlockPos locateBiome(ResourceKey<Biome> biomeKey, ServerLevel level, BlockPos pos) {
        Biome biome = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(biomeKey);
        return locateBiome(biome, level, pos);
    }

    public static BlockPos locateBiome(Biome biome, ServerLevel level, BlockPos pos) {
        return level.findNearestBiome(biome, pos, 6400, 8);
    }

    public static void addFeatureToBiomeCategories(PlacedFeature feature, BiomeCategory biomeCategory, GenerationStep.Decoration generationStep) {
        List<ResourceKey<Biome>> biomeKeys = BIOME_CATEGORY_MAP.get(biomeCategory);
        biomeKeys.forEach(biomeKey -> BiomeHelper.addFeatureToBiome(feature, biomeKey, generationStep));
    }

    public static void addFeatureToBiome(PlacedFeature feature, ResourceKey<Biome> biomeKey, GenerationStep.Decoration generationStep) {
        ResourceKey<PlacedFeature> featureKey;
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

    public static void addStructureToBiomeCategories(ConfiguredStructureFeature<?, ?> structureFeature, BiomeCategory biomeCategory) {
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
