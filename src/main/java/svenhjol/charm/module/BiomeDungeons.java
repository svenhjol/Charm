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

import static svenhjol.charm.base.handler.RegistryHandler.configuredStructureFeature;
import static svenhjol.charm.base.helper.BiomeHelper.addStructureToBiomeCategories;

@Module(mod = Charm.MOD_ID, description = "Dungeon variations with different themes according to the biome.")
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
            .defaultConfig(6, 2, 1225502)
            .register();

        int dungeonSize = 1;

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

        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_badlands"), BADLANDS);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_desert"), DESERT);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_forest"), FOREST);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_jungle"), JUNGLE);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_mountains"), MOUNTAINS);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_nether"), NETHER);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_plains"), PLAINS);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_savanna"), SAVANNA);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_snowy"), SNOWY);
        configuredStructureFeature(new Identifier(Charm.MOD_ID, "dungeon_taiga"), TAIGA);
    }

    @Override
    public void init() {
        DungeonBuilds.init();
        DungeonGenerator.init();

        if (!DungeonGenerator.BADLANDS_DUNGEONS.isEmpty()) addStructureToBiomeCategories(BADLANDS, Biome.Category.MESA);
        if (!DungeonGenerator.DESERT_DUNGEONS.isEmpty()) addStructureToBiomeCategories(DESERT, Biome.Category.DESERT);
        if (!DungeonGenerator.FOREST_DUNGEONS.isEmpty()) addStructureToBiomeCategories(FOREST, Biome.Category.FOREST);
        if (!DungeonGenerator.JUNGLE_DUNGEONS.isEmpty()) addStructureToBiomeCategories(JUNGLE, Biome.Category.JUNGLE);
        if (!DungeonGenerator.MOUNTAINS_DUNGEONS.isEmpty()) addStructureToBiomeCategories(MOUNTAINS, Biome.Category.EXTREME_HILLS);
        if (!DungeonGenerator.NETHER_DUNGEONS.isEmpty()) addStructureToBiomeCategories(MOUNTAINS, Biome.Category.NETHER);
        if (!DungeonGenerator.PLAINS_DUNGEONS.isEmpty()) addStructureToBiomeCategories(PLAINS, Biome.Category.PLAINS);
        if (!DungeonGenerator.SAVANNA_DUNGEONS.isEmpty()) addStructureToBiomeCategories(SAVANNA, Biome.Category.SAVANNA);
        if (!DungeonGenerator.SNOWY_DUNGEONS.isEmpty()) addStructureToBiomeCategories(SNOWY, Biome.Category.ICY);
        if (!DungeonGenerator.TAIGA_DUNGEONS.isEmpty()) addStructureToBiomeCategories(TAIGA, Biome.Category.TAIGA);
    }
}
