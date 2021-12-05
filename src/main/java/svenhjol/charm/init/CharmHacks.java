package svenhjol.charm.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.item.AxeItem;

import java.util.HashMap;

public class CharmHacks {
    public static void init() {
        if (RecipeBookSettings.TAG_FIELDS instanceof ImmutableMap) {
            RecipeBookSettings.TAG_FIELDS = new HashMap<>(RecipeBookSettings.TAG_FIELDS);
        }

        if (AxeItem.STRIPPABLES instanceof ImmutableMap) {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
        }
    }
}
