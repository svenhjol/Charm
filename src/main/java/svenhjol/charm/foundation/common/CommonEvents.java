package svenhjol.charm.foundation.common;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import svenhjol.charm.feature.recipes.SortingRecipeManager;

public final class CommonEvents {
    private static boolean initialized = false;

    public static void runOnce() {
        if (initialized) return;

        // Add our custom recipe sorting handler - should only be added once!
        ResourceManagerHelper.get(PackType.SERVER_DATA)
            .registerReloadListener(new SortingRecipeManager());

        initialized = true;
    }
}
