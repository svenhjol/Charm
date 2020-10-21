package svenhjol.charm.base.handler;

import svenhjol.charm.base.helper.BiomeHelper;

import java.util.Arrays;

public class BiomeHandler {
    public static void init() {
        BiomeHelper.BADLANDS.addAll(Arrays.asList("minecraft:badlands", "minecraft:badlands_plateau", "minecraft:wooded_badlands_plateau"));
        BiomeHelper.DESERT.addAll(Arrays.asList("minecraft:desert", "minecraft:desert_hills", "minecraft:desert_lakes"));
        BiomeHelper.END.addAll(Arrays.asList("minecraft:end_barrens", "minecraft:end_highlands", "minecraft:end_midlands", "minecraft:small_end_islands"));
        BiomeHelper.FOREST.addAll(Arrays.asList("minecraft:forest", "minecraft:wooded_mountains", "minecraft:birch_forest", "minecraft:dark_forest", "minecraft:dark_forest_hills"));
        BiomeHelper.JUNGLE.addAll(Arrays.asList("minecraft:jungle", "minecraft:bamboo_jungle", "minecraft:modified_jungle"));
        BiomeHelper.MOUNTAINS.addAll(Arrays.asList("minecraft:mountains", "minecraft:gravelly_mountains", "minecraft:mountain_edge"));
        BiomeHelper.NETHER.addAll(Arrays.asList("minecraft:soul_sand_valley", "minecraft:crimson_forest", "minecraft:warped_forest", "minecrat:basalt_deltas", "minecraft:nether_wastes"));
        BiomeHelper.PLAINS.addAll(Arrays.asList("minecraft:plains", "minecraft:swamp", "minecraft:sunflower_plains", "minecraft:flower_forest"));
        BiomeHelper.SAVANNA.addAll(Arrays.asList("minecraft:savanna", "minecraft:savanna_plateau", "minecraft:shattered_savanna"));
        BiomeHelper.SNOWY.addAll(Arrays.asList("minecraft:snowy_tundra", "minecraft:snowy_taiga", "minecraft:ice_spikes"));
        BiomeHelper.TAIGA.addAll(Arrays.asList("minecraft:taiga", "minecraft:taiga_hills", "minecraft:taiga_mountains"));
    }
}
