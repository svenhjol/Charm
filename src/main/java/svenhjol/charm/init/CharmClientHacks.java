package svenhjol.charm.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.RecipeBookCategories;

import java.util.HashMap;

public class CharmClientHacks {
    public static void init() {
        if (RecipeBookCategories.AGGREGATE_CATEGORIES instanceof ImmutableMap) {
            RecipeBookCategories.AGGREGATE_CATEGORIES = new HashMap<>(RecipeBookCategories.AGGREGATE_CATEGORIES);
        }
    }
}
