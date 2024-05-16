package svenhjol.charm.feature.core.custom_recipes.common;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import svenhjol.charm.feature.core.custom_recipes.CustomRecipes;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CustomRecipes> {
    public Registers(CustomRecipes feature) {
        super(feature);

        // Register our custom recipe sorting handler.
        ResourceManagerHelper.get(PackType.SERVER_DATA)
            .registerReloadListener(new RecipeSorter());
    }
}
