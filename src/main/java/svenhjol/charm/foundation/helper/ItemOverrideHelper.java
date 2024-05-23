package svenhjol.charm.foundation.helper;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.Log;

public final class ItemOverrideHelper {
    public static final Log LOGGER = new Log(Charm.ID, "ItemOverrideHelper");

    public static void dataComponentValue(Item item, DataComponentType<?> data, Object newValue) {
        var existingComponents = item.components();
        Reference2ObjectArrayMap<DataComponentType<?>, Object> newMap = new Reference2ObjectArrayMap<>();

        for (DataComponentType<?> key : existingComponents.keySet()) {
            var value = key.equals(data) ? newValue : existingComponents.get(key);
            newMap.put(key, value);
        }

        LOGGER.warnIfDebug("Attempting to override " + data.toString() + " component of " + item.getDescriptionId() + " with new value " + newValue);
        item.components = new DataComponentMap.Builder.SimpleMap(newMap);
    }
}
