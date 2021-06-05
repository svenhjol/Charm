package svenhjol.charm.init;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.LootHelper;

import java.util.ArrayList;
import java.util.List;

public class CharmLoot {
    public static List<Identifier> REGISTER = new ArrayList<>();

    public static Identifier VILLAGE_LUMBERJACK = createLootTable("chests/village/village_lumberjack");
    public static Identifier VILLAGE_BEEKEEPER = createLootTable("chests/village/village_beekeeper");
    public static Identifier VILLAGE_LIBRARIAN = createLootTable("chests/village/village_librarian");

    public static void init() {
        LootHelper.CUSTOM_LOOT_TABLES.addAll(REGISTER);
    }

    public static Identifier createLootTable(String name) {
        Identifier id = new Identifier(Charm.MOD_ID, name);
        REGISTER.add(id);
        return id;
    }
}
