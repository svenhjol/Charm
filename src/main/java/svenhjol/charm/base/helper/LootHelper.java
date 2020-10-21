package svenhjol.charm.base.helper;

import net.minecraft.loot.LootTables;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LootHelper {
    public static List<Identifier> CUSTOM_LOOT_TABLES = new ArrayList<>();

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
}