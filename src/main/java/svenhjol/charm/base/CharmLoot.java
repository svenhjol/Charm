package svenhjol.charm.base;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.helper.LootHelper;

public class CharmLoot {
    public static Identifier VILLAGE_LIBRARIAN = new Identifier(Charm.MOD_ID, "chests/village_librarian");

    public static void init() {
        LootHelper.CUSTOM_LOOT_TABLES.add(VILLAGE_LIBRARIAN);
    }
}
