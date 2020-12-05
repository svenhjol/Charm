package svenhjol.charm.base.helper;

import net.minecraft.loot.LootTables;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LootHelper {
    public static List<Identifier> CUSTOM_LOOT_TABLES = new ArrayList<>();
    public static Map<String, Identifier> CACHED_LOOT_TABLES_NAMES = new HashMap<>();

    public static List<Identifier> getAllLootTables() {
        List<Identifier> allLootTables = new ArrayList<>();

        allLootTables.addAll(LootTables.getAll());
        allLootTables.addAll(CUSTOM_LOOT_TABLES);

        return allLootTables;
    }

    public static List<Identifier> getVanillaChestLootTables() {
        return getVanillaLootTables("chests/");
    }

    public static List<Identifier> getVanillaVillageLootTables() {
        return getVanillaLootTables("/village/");
    }

    public static List<Identifier> getVanillaLootTables(String pattern) {
        return LootTables.getAll().stream()
            .filter(t -> t.getPath().contains(pattern))
            .collect(Collectors.toList());
    }

    public static Identifier getLootTable(String loot, Identifier fallback) {
        Identifier lootTable = fallback;

        if (CACHED_LOOT_TABLES_NAMES.isEmpty()) {
            List<Identifier> tables = getAllLootTables();
            for (Identifier table : tables) {
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