package svenhjol.meson.helper;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class LootHelper
{
    public enum RARITY
    {
        COMMON,
        UNCOMMON,
        VALUABLE,
        RARE,
        SPECIAL
    }

    public enum TYPE
    {
        BOOK,
        MISC,
        POTION
    }

    // set up some initial loot table rarity using vanilla tables
    private static Map<RARITY, HashMap<TYPE, List<ResourceLocation>>> lootTables = new HashMap<RARITY, HashMap<TYPE, List<ResourceLocation>>>() {{
        put(RARITY.COMMON, new HashMap<TYPE, List<ResourceLocation>>() {{
            put(TYPE.MISC, new ArrayList<ResourceLocation>() {{
                add(LootTableList.CHESTS_VILLAGE_BLACKSMITH);
                add(LootTableList.CHESTS_IGLOO_CHEST);
            }});
        }});
        put(RARITY.UNCOMMON, new HashMap<TYPE, List<ResourceLocation>>() {{
            put(TYPE.MISC, new ArrayList<ResourceLocation>() {{
                add(LootTableList.CHESTS_SIMPLE_DUNGEON);
                add(LootTableList.CHESTS_JUNGLE_TEMPLE);
                add(LootTableList.CHESTS_WOODLAND_MANSION);
                add(LootTableList.CHESTS_DESERT_PYRAMID);
                add(LootTableList.CHESTS_ABANDONED_MINESHAFT);
            }});
        }});
        put(RARITY.VALUABLE, new HashMap<TYPE, List<ResourceLocation>>() {{
            put(TYPE.MISC, new ArrayList<ResourceLocation>() {{
                add(LootTableList.CHESTS_STRONGHOLD_LIBRARY);
                add(LootTableList.CHESTS_STRONGHOLD_CROSSING);
                add(LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
                add(LootTableList.CHESTS_NETHER_BRIDGE);
            }});
        }});
        put(RARITY.RARE, new HashMap<TYPE, List<ResourceLocation>>() {{
            put(TYPE.MISC, new ArrayList<ResourceLocation>() {{
                add(LootTableList.CHESTS_END_CITY_TREASURE);
            }});
        }});
    }};

    public static ResourceLocation addLootLocation(RARITY rarity, TYPE type, ResourceLocation location)
    {
        checkLootTable(rarity, type);
        lootTables.get(rarity).get(type).add(location);
        return location;
    }

    @SuppressWarnings("unused")
    public static void addLootLocation(RARITY rarity, TYPE type, List<ResourceLocation> locations)
    {
        checkLootTable(rarity, type);
        lootTables.get(rarity).get(type).addAll(locations);
    }

    @SuppressWarnings("unused")
    public static void addToVanillaLoot(String modId, LootTable table, ResourceLocation res)
    {
        LootPool pool = new LootPool(
            new LootEntry[] { new LootEntryTable(res, 1, 0, new LootCondition[0], modId) },
            new LootCondition[0],
            new RandomValueRange(1.0f),
            new RandomValueRange(0.0f),
            modId
        );

        table.addPool(pool);
    }

    public static void addToLootTable(LootTable table, Item item, int weight, int quality, @Nullable LootFunction[] functions, @Nullable LootCondition[] conditions)
    {
        if (functions == null) functions = new LootFunction[0];
        if (conditions == null) conditions = new LootCondition[0];

        LootEntryItem lootItem = new LootEntryItem(item, weight, quality, functions, conditions, Objects.requireNonNull(item.getRegistryName()).toString());
        LootPool pool = table.getPool("main");
        //noinspection ConstantConditions
        if (pool != null) { // this is nullable, so I don't know what table.getPool() is on about
            pool.addEntry(lootItem);
        }
    }

    public static List<ResourceLocation> getLootTables(RARITY rarity, TYPE type)
    {
        checkLootTable(rarity, type);
        return lootTables.get(rarity).get(type).stream().distinct().collect(Collectors.toList());
    }

    public static ResourceLocation getRandomLootTable(RARITY rarity, TYPE type)
    {
        List<ResourceLocation> tables = getLootTables(rarity, type);
        return tables.get(new Random().nextInt(tables.size()));
    }

    private static void checkLootTable(RARITY rarity, TYPE type)
    {
        if (!lootTables.containsKey(rarity)) {
            lootTables.put(rarity, new HashMap<>());
        }
        if (!lootTables.get(rarity).containsKey(type)) {
            lootTables.get(rarity).put(type, new ArrayList<>());
        }
    }
}
