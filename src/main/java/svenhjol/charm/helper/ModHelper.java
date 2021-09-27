package svenhjol.charm.helper;

import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.0-charm
 */
public class ModHelper {
    private static final Map<String, Boolean> CACHED = new HashMap<>();

    public static boolean isLoaded(String mod) {
        boolean result;

        if (!CACHED.containsKey(mod)) {
            FabricLoader instance = FabricLoader.getInstance();
            result = instance.isModLoaded(mod);
            CACHED.put(mod, result);
        } else {
            result = CACHED.get(mod);
        }

        return result;
    }
}
