package svenhjol.charm.init;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import svenhjol.charm.helper.BiomeHelper;

import java.util.ArrayList;

public class CharmBiomes {
    public static void init() {
        BuiltinRegistries.BIOME.entrySet().forEach(entry -> {
            Biome.BiomeCategory category = entry.getValue().getBiomeCategory();
            ResourceKey<Biome> key = entry.getKey();

            BiomeHelper.BIOME_CATEGORY_MAP.putIfAbsent(category, new ArrayList<>());
            BiomeHelper.BIOME_CATEGORY_MAP.get(category).add(key);
        });
    }
}
