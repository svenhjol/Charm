package svenhjol.charm.charmony.helper;

import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charm.charmony.Charmony;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.enums.Side;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class ConfigHelper {
    private static final Log LOGGER = new Log(Charmony.ID, "ConfigHelper");

    public static final String DEBUG_MODE = "Debug mode";
    public static final String MIXIN_DISABLE_MODE = "Mixin disable mode";

    public static boolean isModLoaded(String id) {
        FabricLoader instance = FabricLoader.getInstance();
        return instance.isModLoaded(id);
    }

    public static boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isFeatureEnabled(String modId, String featureName) {
        Feature annotation;

        if (featureName.contains(".")) {
            var split = featureName.split("\\.");
            if (split.length > 2) {
                LOGGER.error("Feature name can't be parsed, too many fragments");
                return false;
            }
            featureName = split[0];
        }

        try {
            var clazz = Class.forName("svenhjol." + modId + ".feature." + TextHelper.upperCamelToSnake(featureName) + "." + featureName);
            annotation = clazz.getAnnotation(Feature.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Missing feature annotation: " + featureName);
        }

        var path = featureName + ".Enabled";
        var allSidesAreValid = true;
        var configExists = false;

        for (Side side : Side.values()) {
            var fileName = filename(modId, side);
            if (!configExists(fileName)) {
                continue;
            }
            
            configExists = true;
            var toml = read(fileName);
            
            if (toml.contains(path)) {
                // If the "Enabled" path can be found in the config, use its value.
                allSidesAreValid = allSidesAreValid && toml.getBoolean(path);
            }
        }
        
        return configExists ? allSidesAreValid : annotation.enabledByDefault();
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

    public static Toml read(String filename) {
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
}
