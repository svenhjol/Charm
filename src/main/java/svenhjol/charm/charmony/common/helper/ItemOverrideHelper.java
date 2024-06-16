package svenhjol.charm.charmony.common.helper;

import net.minecraft.world.item.Item;
import svenhjol.charm.charmony.Charmony;
import svenhjol.charm.charmony.Log;

public final class ItemOverrideHelper {
    public static final Log LOGGER = new Log(Charmony.ID, "ItemOverrideHelper");

    public static void maxStackSize(Item item, int newValue) {
        item.maxStackSize = newValue;
    }
}
