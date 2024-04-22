package svenhjol.charm.foundation.helper;

import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.diagnostics.Diagnostics;
import svenhjol.charm.foundation.enums.Side;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class ConfigHelper {
    private static boolean hasCheckedDebugConfig = false;
    private static boolean hasCheckedCompatConfig = false;
    private static boolean cachedDebugValue = false;
    private static boolean cachedCompatValue = false;

    public static boolean isModLoaded(String id) {
        FabricLoader instance = FabricLoader.getInstance();
        return instance.isModLoaded(id);
    }

    /**
     * Helper method to check if debug mode is enabled in Charm's config file.
     * In Fabric, running Charm in the dev environment will always return true.
     */
    public static boolean isDebugEnabled() {
        var isDevEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment();
        if (isDevEnvironment) return true;

        if (!hasCheckedDebugConfig) {
            var toml = read(filename(Charm.ID, Side.COMMON));
            var key = "Diagnostics.\"" + Diagnostics.DEBUG_MODE + "\"";
            if (toml.contains(key)) {
                cachedDebugValue = toml.getBoolean(key);
            }

            hasCheckedDebugConfig = true;
        }

        return cachedDebugValue;
    }

    /**
     * Helper method to check if compat mode is enabled in Charm's config file.
     */
    public static boolean isCompatEnabled() {
        if (!hasCheckedCompatConfig) {
            var toml = read(filename(Charm.ID, Side.COMMON));
            var key = "Diagnostics.\"" + Diagnostics.COMPAT_MODE + "\"";
            if (toml.contains(key)) {
                cachedCompatValue = toml.getBoolean(key);
            }

            hasCheckedCompatConfig = true;
        }

        return cachedCompatValue;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isFeatureEnabled(String filename, String featureName) {
        var toml = read(filename);
        var quoted = quotedFeatureEnabledName(featureName);
        var path = featureName + "." + quoted;

        if (toml.contains(path)) {
            return toml.getBoolean(path);
        }

        return true;
    }

    public static boolean configExists(String filename) {
        return configPath(filename).toFile().exists();
    }

    public static Path configDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static String filename(String modId, Side side) {
        return modId + "-" + side.getName();
    }

    static Toml read(String filename) {
        var toml = new Toml();
        var path = configPath(filename);
        var file = path.toFile();

        if (!file.exists()) {
            return toml;
        }

        return toml.read(file);
    }

    static Path configPath(String filename) {
        return Paths.get(configDir() + "/" + filename + ".toml");
    }

    static String quotedFeatureEnabledName(String featureName) {
        var featureEnabled = featureName + " enabled";
        return "\"" + featureEnabled + "\"";
    }
}
