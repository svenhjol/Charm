package svenhjol.meson.helper;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

public class LootHelper
{
    public static void addTableEntry(LootTable table, LootEntry entry)
    {
        List<LootPool> pools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c");
        if (pools == null) return;
        LootPool pool = pools.get(0);

        List<LootEntry> entries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186453_a");
        if (entries == null) return;
        entries.add(entry);
    }
}
