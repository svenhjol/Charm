package svenhjol.charm.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0.0-charm
 */
public class LootHelper {
    public static List<ResourceLocation> CUSTOM_LOOT_TABLES = new ArrayList<>();
    public static Map<String, ResourceLocation> CACHED_LOOT_TABLES_NAMES = new HashMap<>();

    public static List<ResourceLocation> getAllLootTables() {
        List<ResourceLocation> allLootTables = new ArrayList<>();

        allLootTables.addAll(BuiltInLootTables.all());
        allLootTables.addAll(CUSTOM_LOOT_TABLES);

        return allLootTables;
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

    @Nullable
    public static Map<Integer, List<Item>> loadItemsFromResource(MinecraftServer server, ResourceLocation table) {
        Map<Integer, List<Item>> map = new HashMap<>();
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

        var resource = server.getResourceManager().getResource(table).orElse(null);
        if (resource == null) {
            return null;
        }

        InputStream inputStream;

        try {
            inputStream = resource.open();
        } catch (IOException e) {
            return null;
        }

        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        JsonObject obj = GsonHelper.fromJson(gson, reader, JsonObject.class);
        if (obj == null) return null;
        JsonArray pools = GsonHelper.getAsJsonArray(obj, "pools");

        for (int p = 0; p < pools.size(); p++) {
            JsonArray elements = GsonHelper.getAsJsonArray((JsonObject) pools.get(p), "entries");
            for (int e = 0; e < elements.size(); e++) {
                JsonObject entry = (JsonObject) elements.get(e);
                String name = entry.get("name").getAsString();
                ResourceLocation res = new ResourceLocation(name);

                // try instantiate
                if (Registry.ITEM.getOptional(res).isPresent()) {
                    map.computeIfAbsent(p, a -> new LinkedList<>()).add(Registry.ITEM.get(res));
                } else {
                    LogHelper.debug(LootHelper.class, "Could not find item in registry: " + res);
                }
            }
        }

        return map;
    }
}