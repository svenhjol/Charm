package svenhjol.charm.init;

import svenhjol.charm.Charm;
import svenhjol.charm.helper.LootHelper;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class CharmLoot {
    public static List<ResourceLocation> REGISTER = new ArrayList<>();

    public static ResourceLocation VILLAGE_LUMBERJACK = createLootTable("chests/village/village_lumberjack");
    public static ResourceLocation VILLAGE_BEEKEEPER = createLootTable("chests/village/village_beekeeper");
    public static ResourceLocation VILLAGE_LIBRARIAN = createLootTable("chests/village/village_librarian");

    public static void init() {
        LootHelper.CUSTOM_LOOT_TABLES.addAll(REGISTER);
    }

    public static ResourceLocation createLootTable(String name) {
        ResourceLocation id = new ResourceLocation(Charm.MOD_ID, name);
        REGISTER.add(id);
        return id;
    }
}
