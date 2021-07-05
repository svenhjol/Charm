package svenhjol.charm.helper;

import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charm.module.core.Core;

/**
 * @version 1.0.0-charm
 */
public class DebugHelper {
    private static final boolean isDevelopmentEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static boolean isDebugMode() {
        return isDevelopmentEnvironment || Core.debug;
    }
}
