package svenhjol.charm.init;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import svenhjol.charm.helper.BiomeHelper;

import java.util.ArrayList;

public class CharmBiomes {
    public static void init() {
        BuiltinRegistries.BIOME.getEntries().forEach(entry -> {
            Biome.Category category = entry.getValue().getCategory();
            RegistryKey<Biome> key = entry.getKey();

            BiomeHelper.BIOME_CATEGORY_MAP.putIfAbsent(category, new ArrayList<>());
            BiomeHelper.BIOME_CATEGORY_MAP.get(category).add(key);
        });
    }
}
