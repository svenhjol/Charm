package svenhjol.charm.module;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.structure.DungeonBuilds;
import svenhjol.charm.structure.DungeonFeature;
import svenhjol.charm.structure.DungeonGenerator;

import static svenhjol.charm.base.handler.RegistryHandler.configuredFeature;
import static svenhjol.charm.base.helper.BiomeHelper.addStructureFeatureToBiomes;

@Module(mod = Charm.MOD_ID)
public class BiomeDungeons extends CharmModule {
    public static final Identifier DUNGEON_ID = new Identifier(Charm.MOD_ID, "dungeon");
    public static StructureFeature<StructurePoolFeatureConfig> DUNGEON_FEATURE;

    public static ConfiguredStructureFeature<?, ?> BADLANDS;
    public static ConfiguredStructureFeature<?, ?> DESERT;
    public static ConfiguredStructureFeature<?, ?> FOREST;
    public static ConfiguredStructureFeature<?, ?> JUNGLE;
    public static ConfiguredStructureFeature<?, ?> MOUNTAINS;
    public static ConfiguredStructureFeature<?, ?> NETHER;
    public static ConfiguredStructureFeature<?, ?> PLAINS;
    public static ConfiguredStructureFeature<?, ?> SAVANNA;
    public static ConfiguredStructureFeature<?, ?> SNOWY;
    public static ConfiguredStructureFeature<?, ?> TAIGA;

    @Override
    public void register() {
        DUNGEON_FEATURE = new DungeonFeature(StructurePoolFeatureConfig.CODEC);

        FabricStructureBuilder.create(DUNGEON_ID, DUNGEON_FEATURE)
            .step(GenerationStep.Feature.STRONGHOLDS)
            .defaultConfig(2, 0, 1225502)
            .register();

        int dungeonSize = 2;

        BADLANDS    = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.BADLANDS_POOL, dungeonSize));
        DESERT      = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.DESERT_POOL, dungeonSize));
        FOREST      = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.FOREST_POOL, dungeonSize));
        JUNGLE      = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.JUNGLE_POOL, dungeonSize));
        MOUNTAINS   = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.MOUNTAINS_POOL, dungeonSize));
        NETHER      = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.NETHER_POOL, dungeonSize));
        PLAINS      = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.PLAINS_POOL, dungeonSize));
        SAVANNA     = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.SAVANNA_POOL, dungeonSize));
        SNOWY       = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.SNOWY_POOL, dungeonSize));
        TAIGA       = DUNGEON_FEATURE.configure(new StructurePoolFeatureConfig(() -> DungeonGenerator.TAIGA_POOL, dungeonSize));

        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_badlands"), BADLANDS);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_desert"), DESERT);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_forest"), FOREST);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_jungle"), JUNGLE);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_mountains"), MOUNTAINS);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_nether"), NETHER);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_plains"), PLAINS);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_savanna"), SAVANNA);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_snowy"), SNOWY);
        configuredFeature(new Identifier(Charm.MOD_ID, "dungeon_taiga"), TAIGA);
    }

    @Override
    public void init() {
        DungeonBuilds.init();
        DungeonGenerator.init();

        if (!DungeonGenerator.BADLANDS_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.MESA, BADLANDS);
        if (!DungeonGenerator.DESERT_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.DESERT, DESERT);
        if (!DungeonGenerator.FOREST_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.FOREST, FOREST);
        if (!DungeonGenerator.JUNGLE_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.JUNGLE, JUNGLE);
        if (!DungeonGenerator.MOUNTAINS_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.EXTREME_HILLS, MOUNTAINS);
        if (!DungeonGenerator.NETHER_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.NETHER, MOUNTAINS);
        if (!DungeonGenerator.PLAINS_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.PLAINS, PLAINS);
        if (!DungeonGenerator.SAVANNA_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.SAVANNA, SAVANNA);
        if (!DungeonGenerator.SNOWY_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.ICY, SNOWY);
        if (!DungeonGenerator.TAIGA_DUNGEONS.isEmpty()) addStructureFeatureToBiomes(Biome.Category.TAIGA, TAIGA);
    }
}
