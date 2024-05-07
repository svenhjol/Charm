package svenhjol.charm.feature.silence_microsoft;

import net.fabricmc.loader.api.FabricLoader;

public final class CommonCallbacks {
    public static boolean disableDevEnvironmentConnections() {
        return SilenceMicrosoft.disableDevEnvironmentConnections
            && FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
