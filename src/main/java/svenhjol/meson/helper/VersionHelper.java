package svenhjol.meson.helper;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class VersionHelper {
    public static MainWindow getMainWindow(Minecraft mc) {
//        return mc.mainWindow; // 1.14
        return mc.getMainWindow(); // 1.15
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addStructureToBiome(Structure structure, Biome biome) {
        // 1.14
//        biome.addFeature(
//            GenerationStage.Decoration.UNDERGROUND_STRUCTURES,
//            Biome.createDecoratedFeature(structure, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
//
//        biome.addStructure(structure, IFeatureConfig.NO_FEATURE_CONFIG);

        // 1.15
        final ConfiguredFeature configured = structure.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG);
        final ConfiguredFeature decorated = configured.withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));

        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, decorated);
        biome.addStructure(configured);
    }
}
