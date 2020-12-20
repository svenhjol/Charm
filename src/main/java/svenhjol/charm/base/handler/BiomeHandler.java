package svenhjol.charm.base.handler;

import net.minecraft.world.biome.BiomeKeys;
import svenhjol.charm.base.helper.BiomeHelper;

import java.util.Arrays;

public class BiomeHandler {
    public static void init() {
        BiomeHelper.BADLANDS.addAll(Arrays.asList(BiomeKeys.BADLANDS, BiomeKeys.BADLANDS_PLATEAU, BiomeKeys.WOODED_BADLANDS_PLATEAU));
        BiomeHelper.DESERT.addAll(Arrays.asList(BiomeKeys.DESERT, BiomeKeys.DESERT_HILLS, BiomeKeys.DESERT_LAKES));
        BiomeHelper.END.addAll(Arrays.asList(BiomeKeys.END_BARRENS, BiomeKeys.END_HIGHLANDS, BiomeKeys.END_MIDLANDS, BiomeKeys.SMALL_END_ISLANDS));
        BiomeHelper.FOREST.addAll(Arrays.asList(BiomeKeys.FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.DARK_FOREST, BiomeKeys.DARK_FOREST_HILLS));
        BiomeHelper.JUNGLE.addAll(Arrays.asList(BiomeKeys.JUNGLE, BiomeKeys.BAMBOO_JUNGLE, BiomeKeys.MODIFIED_JUNGLE));
        BiomeHelper.MOUNTAINS.addAll(Arrays.asList(BiomeKeys.MOUNTAINS, BiomeKeys.GRAVELLY_MOUNTAINS, BiomeKeys.WOODED_MOUNTAINS, BiomeKeys.SNOWY_MOUNTAINS));
        BiomeHelper.NETHER.addAll(Arrays.asList(BiomeKeys.SOUL_SAND_VALLEY, BiomeKeys.CRIMSON_FOREST, BiomeKeys.WARPED_FOREST, BiomeKeys.BASALT_DELTAS, BiomeKeys.NETHER_WASTES));
        BiomeHelper.PLAINS.addAll(Arrays.asList(BiomeKeys.PLAINS, BiomeKeys.SWAMP, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.FLOWER_FOREST));
        BiomeHelper.SAVANNA.addAll(Arrays.asList(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.SHATTERED_SAVANNA));
        BiomeHelper.SNOWY.addAll(Arrays.asList(BiomeKeys.SNOWY_TUNDRA, BiomeKeys.SNOWY_TAIGA, BiomeKeys.ICE_SPIKES));
        BiomeHelper.TAIGA.addAll(Arrays.asList(BiomeKeys.TAIGA, BiomeKeys.TAIGA_HILLS, BiomeKeys.TAIGA_MOUNTAINS));
    }
}
