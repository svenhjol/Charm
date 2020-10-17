package svenhjol.charm.base.helper;

import net.fabricmc.loader.api.FabricLoader;

public class ModHelper {
    public static boolean isLoaded(String mod) {
        FabricLoader instance = FabricLoader.getInstance();
        boolean result = instance.isModLoaded(mod);
        return result;
    }
}
