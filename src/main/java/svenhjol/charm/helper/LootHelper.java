package svenhjol.charm.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class LootHelper {
    public static List<ResourceLocation> CUSTOM_LOOT_TABLES = new ArrayList<>();
    public static Map<String, ResourceLocation> CACHED_LOOT_TABLES_NAMES = new HashMap<>();

    public static List<ResourceLocation> getAllLootTables() {
        List<ResourceLocation> allLootTables = new ArrayList<>();

        allLootTables.addAll(BuiltInLootTables.all());
        allLootTables.addAll(CUSTOM_LOOT_TABLES);

        return allLootTables;
    }

    public static List<ResourceLocation> getVanillaChestLootTables() {
        return getVanillaLootTables("chests/");
    }

    public static List<ResourceLocation> getVanillaVillageLootTables() {
        return getVanillaLootTables("/village/");
    }

    public static List<ResourceLocation> getVanillaLootTables(String pattern) {
        return BuiltInLootTables.all().stream()
            .filter(t -> t.getPath().contains(pattern))
            .collect(Collectors.toList());
    }

    public static ResourceLocation getLootTable(String loot, ResourceLocation fallback) {
        ResourceLocation lootTable = fallback;

        if (CACHED_LOOT_TABLES_NAMES.isEmpty()) {
            List<ResourceLocation> tables = getAllLootTables();
            for (ResourceLocation table : tables) {
                String[] s = table.getPath().split("/");
                String last = s[s.length - 1];
                CACHED_LOOT_TABLES_NAMES.put(last, table);
            }
        }

        if (!loot.isEmpty()) {
            for (String s : CACHED_LOOT_TABLES_NAMES.keySet()) {
                if (s.contains(loot)) {
                    lootTable = CACHED_LOOT_TABLES_NAMES.get(s);
                    break;
                }
            }
        }

        return lootTable;
    }
}