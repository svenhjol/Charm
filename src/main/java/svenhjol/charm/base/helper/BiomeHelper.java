package svenhjol.charm.base.helper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.impl.biome.modification.BiomeModificationContextImpl;
import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.GenerationSettingsAccessor;
import svenhjol.charm.mixin.accessor.SpawnSettingsAccessor;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings({"UnstableApiUsage", "unused", "deprecation"})
public class BiomeHelper {
    public static List<RegistryKey<Biome>> BADLANDS = new ArrayList<>();
    public static List<RegistryKey<Biome>> DESERT = new ArrayList<>();
    public static List<RegistryKey<Biome>> END = new ArrayList<>();
    public static List<RegistryKey<Biome>> FOREST = new ArrayList<>();
    public static List<RegistryKey<Biome>> JUNGLE = new ArrayList<>();
    public static List<RegistryKey<Biome>> MOUNTAINS = new ArrayList<>();
    public static List<RegistryKey<Biome>> NETHER = new ArrayList<>();
    public static List<RegistryKey<Biome>> PLAINS = new ArrayList<>();
    public static List<RegistryKey<Biome>> SAVANNA = new ArrayList<>();
    public static List<RegistryKey<Biome>> SNOWY = new ArrayList<>();
    public static List<RegistryKey<Biome>> TAIGA = new ArrayList<>();

    public static Biome getBiome(ServerWorld world, BlockPos pos) {
        BiomeAccess biomeAccess = world.getBiomeAccess();
        return biomeAccess.getBiome(pos);
    }

    public static Biome getBiomeFromBiomeKey(RegistryKey<Biome> biomeKey) {
        return BuiltinRegistries.BIOME.get(biomeKey);
    }

    public static Optional<RegistryKey<Biome>> getBiomeKeyAtPosition(ServerWorld world, BlockPos pos) {
        return world.method_31081(pos);
    }

    public static BlockPos locateBiome(RegistryKey<Biome> biomeKey, ServerWorld world, BlockPos pos) {
        Biome biome = world.getRegistryManager().get(Registry.BIOME_KEY).get(biomeKey);
        return locateBiome(biome, world, pos);
    }

    public static BlockPos locateBiome(Biome biome, ServerWorld world, BlockPos pos) {
        return world.locateBiome(biome, pos, 6400, 8);
    }

    public static void addStructureFeatureToBiomes(List<RegistryKey<Biome>> biomeKeys, ConfiguredStructureFeature<?, ?> configuredFeature) {
        biomeKeys.forEach(biomeKey -> BiomeHelper.addStructureFeature(biomeKey, configuredFeature));
    }

    public static void addStructureFeature(RegistryKey<Biome> biomeKey, ConfiguredStructureFeature<?, ?> structureFeature) {
        RegistryKey<ConfiguredStructureFeature<?, ?>> structureKey;
        Predicate<BiomeSelectionContext> biomeSelector;

        try {
            biomeSelector = BiomeSelectors.includeByKey(biomeKey);
            structureKey = BuiltInRegistryKeys.get(structureFeature);
        } catch (Exception e) {
            Charm.LOG.error("Failed to add structure to biome. This may cause crashes when trying to locate the structure.");
            return;
        }

        BiomeModifications.addStructure(biomeSelector, structureKey);
    }

    public static void addSpawnEntry(RegistryKey<Biome> biomeKey, SpawnGroup group, EntityType<?> entity, int weight, int minGroupSize, int maxGroupSize) {
        try {
            Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(biomeKey);
            BiomeModifications.addSpawn(biomeSelector, group, entity, weight, minGroupSize, maxGroupSize);
        } catch (Exception e) {
            Charm.LOG.error("Failed to add entity to biome spawn. This may cause crashes when trying to spawn the entity.");
        }
    }
}
