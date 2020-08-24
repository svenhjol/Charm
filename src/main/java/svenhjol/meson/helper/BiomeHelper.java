package svenhjol.meson.helper;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import svenhjol.charm.mixin.accessor.GenerationSettingsAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BiomeHelper {
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
