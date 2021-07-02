package svenhjol.charm.helper;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlWriter;
import com.moandjiezana.toml.Toml;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

import java.io.File;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ConfigHelper {
    public static Toml readConfig(String modId) {
        Path path = getConfigPath(modId);
        File file = path.toFile();

        if (!file.exists())
            return new Toml();

        return new Toml().read(path.toFile());
    }

    public static boolean isModuleDisabled(String modId, String moduleName) {
        return isModuleDisabled(readConfig(modId), moduleName);
    }

    public static boolean isModuleDisabled(Toml toml, String moduleName) {
        String moduleEnabled = moduleName + " Enabled";
        String moduleEnabledQuoted = "\"" + moduleEnabled + "\"";
        return toml.contains(moduleEnabledQuoted) && !toml.getBoolean(moduleEnabledQuoted);
    }

    public static <T extends CharmModule> void applyConfig(String mod, List<T> modules) {
        Toml toml = readConfig(mod);

        modules.forEach(module -> {
            String moduleName = module.getName();
            module.setEnabledInConfig(!isModuleDisabled(toml, moduleName));

            // get and set module config options
            ArrayList<Field> classFields = new ArrayList<>(Arrays.asList(module.getClass().getDeclaredFields()));
            for (Field prop : classFields) {
                try {
                    Config annotation = prop.getDeclaredAnnotation(Config.class);
                    if (annotation == null)
                        continue;

                    // set the static property as writable so that the config can modify it
                    prop.setAccessible(true);
                    String propName = annotation.name();

                    if (propName.isEmpty())
                        propName = prop.getName();

                    Object propValue = prop.get(null);
                    Object configValue = null;

                    if (toml.contains(moduleName)) {

                        // get the block of key/value pairs from the config
                        Toml moduleKeys = toml.getTable(moduleName);
                        Map<String, Object> mappedKeys = new HashMap<>();

                        // key names sometimes have quotes, map to remove them
                        moduleKeys.toMap().forEach((k, v) -> mappedKeys.put(k.replace("\"", ""), v));
                        configValue = mappedKeys.get(propName);

                        if (configValue != null) {

                            // there's some weirdness with casting, deal with that here
                            if (propValue instanceof Integer && configValue instanceof Double)
                                configValue = (int)(double) configValue;

                            if (propValue instanceof Integer && configValue instanceof Long)
                                configValue = (int)(long) configValue;

                            // set the class property
                            Charm.LOG.debug("[ConfigHelper] In module `" + moduleName + "`, setting `" + propName + "` to `" + configValue + "`");
                            prop.set(null, configValue);
                        }
                    }

                } catch (Exception e) {
                    Charm.LOG.error("[ConfigHelper] Failed to read config for `" + moduleName + "`: " + e.getMessage());
                }
            }
        });
    }

    public static <T extends CharmModule> void writeConfig(String modId, List<T> modules) {
        Path path = getConfigPath(modId);

        // this blank config is appended and then written out. LinkedHashMap supplier sorts the contents alphabetically
        CommentedConfig config = TomlFormat.newConfig(LinkedHashMap::new);

        // read config from each module
        modules.forEach(module -> {
            String moduleName = module.getName();

            if (!module.isAlwaysEnabled()) {
                String field = moduleName + " Enabled";
                config.setComment(field, module.getDescription());
                config.add(field, module.isEnabledInConfig());
            }

            // read class properties into config
            ArrayList<Field> classFields = new ArrayList<>(Arrays.asList(module.getClass().getDeclaredFields()));
            classFields.forEach(prop -> {
                try {
                    Config annotation = prop.getDeclaredAnnotation(Config.class);
                    if (annotation == null) return;

                    String propName = annotation.name();
                    String propDescription = annotation.description();
                    Object propValue = prop.get(null);

                    // set the key/value pair. The "." specifies that it is nested
                    String moduleConfigName = moduleName + "." + propName;
                    config.setComment(moduleConfigName, propDescription);
                    config.add(moduleConfigName, propValue);

                } catch (Exception e) {
                    Charm.LOG.error("[ConfigHelper] Failed to write config property `" + prop.getName() + "` in `" + module.getName() + "`");
                }
            });
        });

        try {
            // write out and close the file
            TomlWriter tomlWriter = new TomlWriter();
            Writer buffer = Files.newBufferedWriter(path);
            tomlWriter.write(config, buffer);
            buffer.close();
            Charm.LOG.debug("[ConfigHelper] Written config to disk");
        } catch (Exception e) {
            Charm.LOG.error("[ConfigHelper] Failed to write config: " + e.getMessage());
        }
    }

    private static Path getConfigPath(String modId) {
        String configPath = "./config/" + modId + ".toml";
        return Paths.get(configPath);
    }
}
