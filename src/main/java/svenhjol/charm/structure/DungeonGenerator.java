package svenhjol.charm.structure;

import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.structure.BaseGenerator;
import svenhjol.charm.base.structure.BaseStructure;

import java.util.ArrayList;
import java.util.List;

public class DungeonGenerator extends BaseGenerator {
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

    public static List<BaseStructure> BADLANDS_RUINS = new ArrayList<>();
    public static List<BaseStructure> DESERT_RUINS = new ArrayList<>();
    public static List<BaseStructure> FOREST_RUINS = new ArrayList<>();
    public static List<BaseStructure> JUNGLE_RUINS = new ArrayList<>();
    public static List<BaseStructure> MOUNTAINS_RUINS = new ArrayList<>();
    public static List<BaseStructure> NETHER_RUINS = new ArrayList<>();
    public static List<BaseStructure> PLAINS_RUINS = new ArrayList<>();
    public static List<BaseStructure> SAVANNA_RUINS = new ArrayList<>();
    public static List<BaseStructure> SNOWY_RUINS = new ArrayList<>();
    public static List<BaseStructure> TAIGA_RUINS = new ArrayList<>();

    public static void init() {
        BADLANDS_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/badlands/starts"), BADLANDS_RUINS);
        DESERT_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/desert/starts"), DESERT_RUINS);
        FOREST_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/forest/starts"), FOREST_RUINS);
        JUNGLE_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/jungle/starts"), JUNGLE_RUINS);
        MOUNTAINS_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/mountains/starts"), MOUNTAINS_RUINS);
        NETHER_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/nether/starts"), NETHER_RUINS);
        PLAINS_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/plains/starts"), PLAINS_RUINS);
        SAVANNA_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/savanna/starts"), SAVANNA_RUINS);
        SNOWY_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/snowy/starts"), SNOWY_RUINS);
        TAIGA_POOL = registerPool(new Identifier(Charm.MOD_ID, "dungeons/taiga/starts"), TAIGA_RUINS);
    }
}
