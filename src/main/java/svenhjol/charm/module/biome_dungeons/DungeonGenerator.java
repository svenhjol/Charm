package svenhjol.charm.module.biome_dungeons;

import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.world.CharmGenerator;
import svenhjol.charm.world.CharmStructure;

import java.util.ArrayList;
import java.util.List;

public class DungeonGenerator extends CharmGenerator {
    public static StructurePool BADLANDS_POOL;
    public static StructurePool DESERT_POOL;
    public static StructurePool FOREST_POOL;
    public static StructurePool JUNGLE_POOL;
    public static StructurePool MOUNTAINS_POOL;
    public static StructurePool NETHER_POOL;
    public static StructurePool PLAINS_POOL;
    public static StructurePool SAVANNA_POOL;
    public static StructurePool SNOWY_POOL;
    public static StructurePool TAIGA_POOL;

    public static List<CharmStructure> BADLANDS_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> DESERT_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> FOREST_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> JUNGLE_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> MOUNTAINS_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> NETHER_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> PLAINS_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> SAVANNA_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> SNOWY_DUNGEONS = new ArrayList<>();
    public static List<CharmStructure> TAIGA_DUNGEONS = new ArrayList<>();

    public static void init() {
        BADLANDS_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/badlands/starts"), BADLANDS_DUNGEONS);
        DESERT_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/desert/starts"), DESERT_DUNGEONS);
        FOREST_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/forest/starts"), FOREST_DUNGEONS);
        JUNGLE_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/jungle/starts"), JUNGLE_DUNGEONS);
        MOUNTAINS_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/mountains/starts"), MOUNTAINS_DUNGEONS);
        NETHER_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/nether/starts"), NETHER_DUNGEONS);
        PLAINS_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/plains/starts"), PLAINS_DUNGEONS);
        SAVANNA_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/savanna/starts"), SAVANNA_DUNGEONS);
        SNOWY_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/snowy/starts"), SNOWY_DUNGEONS);
        TAIGA_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/taiga/starts"), TAIGA_DUNGEONS);
    }
}
