package svenhjol.charm.base.helper;

import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class StructureHelper {
    public static List<StructureProcessor> SINGLE_POOL_ELEMENT_PROCESSORS = new ArrayList<>();

    public static void addToBiome(List<String> biomeGroup, ConfiguredStructureFeature<?, ?> configuredFeature) {
        biomeGroup.forEach(id -> BuiltinRegistries.BIOME.getOrEmpty(new Identifier(id))
            .ifPresent(biome -> BiomeHelper.addStructureFeature(biome, configuredFeature)));
    }
}
