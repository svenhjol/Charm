package svenhjol.meson.helper;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LootHelper
{
    public static List<ResourceLocation> customTables = new ArrayList<>();

    public static void addTableEntry(LootTable table, LootEntry entry)
    {
        List<LootPool> pools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c");
        if (pools == null) return;
        LootPool pool = pools.get(0);

        List<LootEntry> entries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186453_a");
        if (entries == null) return;
        entries.add(entry);
    }

    public static List<ResourceLocation> getLootTables()
    {
        List<ResourceLocation> tables = new ArrayList<>();

        List<ResourceLocation> vanillaChests = LootTables.func_215796_a().stream()
            .filter(r -> r.getPath().contains("chest"))
            .collect(Collectors.toList());

        tables.addAll(vanillaChests);
        tables.addAll(customTables);

        return tables;
    }
}
