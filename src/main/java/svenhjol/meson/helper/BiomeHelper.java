package svenhjol.meson.helper;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import svenhjol.meson.mixin.accessor.GenerationSettingsAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BiomeHelper {
    public static List<String> BADLANDS = new ArrayList<>();
    public static List<String> DESERT = new ArrayList<>();
    public static List<String> END = new ArrayList<>();
    public static List<String> FOREST = new ArrayList<>();
    public static List<String> JUNGLE = new ArrayList<>();
    public static List<String> MOUNTAINS = new ArrayList<>();
    public static List<String> NETHER = new ArrayList<>();
    public static List<String> PLAINS = new ArrayList<>();
    public static List<String> SAVANNA = new ArrayList<>();
    public static List<String> SNOWY = new ArrayList<>();
    public static List<String> TAIGA = new ArrayList<>();

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

    public static void addStructureFeature(Biome biome, ConfiguredStructureFeature<?, ?> structureFeature) {
        GenerationSettings settings = biome.getGenerationSettings();
        checkGenerationSettingsMutable(settings);
        ((GenerationSettingsAccessor)settings).getStructureFeatures().add(() -> structureFeature);
    }

    /**
     * Evil hack until there's a better way to add structures to biomes
     */
    private static void checkGenerationSettingsMutable(GenerationSettings settings) {
        List<Supplier<ConfiguredStructureFeature<?, ?>>> existing = ((GenerationSettingsAccessor)settings).getStructureFeatures();
        if (existing instanceof ImmutableList)
            ((GenerationSettingsAccessor)settings).setStructureFeatures(new ArrayList<>(existing));
    }
}
