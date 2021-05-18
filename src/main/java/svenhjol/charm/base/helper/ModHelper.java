package svenhjol.charm.base.helper;

import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.Map;

public class ModHelper {
    private static final Map<String, Boolean> cachedLoaded = new HashMap<>();

    public static boolean isLoaded(String mod) {
        boolean result;

        if (!cachedLoaded.containsKey(mod)) {
            FabricLoader instance = FabricLoader.getInstance();
            result = instance.isModLoaded(mod);
            cachedLoaded.put(mod, result);
        } else {
            result = cachedLoaded.get(mod);
        }

        return result;
    }
}
