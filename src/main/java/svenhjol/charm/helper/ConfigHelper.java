package svenhjol.charm.helper;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlWriter;
import com.moandjiezana.toml.Toml;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmCommonModule;

import javax.annotation.Nullable;
import java.io.File;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ConfigHelper {

    @Nullable
    public static Toml getConfig(String mod) {
        String configPath = "./config/" + mod + ".toml";
        Path path = Paths.get(configPath);
        File file = path.toFile();

        if (!file.exists())
            return null;

        return new Toml().read(path.toFile());
    }

    public static boolean isModuleDisabled(Toml config, String moduleName) {
        String moduleEnabled = moduleName + " Enabled";
        String moduleEnabledQuoted = "\"" + moduleEnabled + "\"";
        return config.contains(moduleEnabledQuoted) && !config.getBoolean(moduleEnabledQuoted);
    }

    public static<T extends CharmCommonModule> void createConfig(String mod, Map<String, T> modules) {
        String configPath = "./config/" + mod + ".toml";

        // this blank config is appended and then written out. LinkedHashMap supplier sorts the contents alphabetically
        CommentedConfig writeConfig = TomlFormat.newConfig(LinkedHashMap::new);
        Path path = Paths.get(configPath);
        File file = path.toFile();
        Toml readConfig = file.exists() ? new Toml().read(path.toFile()) : new Toml();

        List<String> moduleNames = new ArrayList<>(modules.keySet());
        Collections.sort(moduleNames);

        // parse config and apply values to modules
        for (String moduleName : moduleNames) {
            T module = modules.get(moduleName);

            // set module enabled/disabled
            String moduleEnabled = moduleName + " Enabled";
            String moduleEnabledQuoted = "\"" + moduleEnabled + "\"";
            boolean enabled = readConfig.contains(moduleEnabledQuoted) ? readConfig.getBoolean(moduleEnabledQuoted) : module.isEnabledByDefault();
            module.setEnabled(enabled);

            if (!module.isAlwaysEnabled()) {
                writeConfig.setComment(moduleEnabled, module.getDescription());
                writeConfig.add(moduleEnabled, module.isEnabled());
            }

            // get and set module config options
            ArrayList<Field> classFields = new ArrayList<>(Arrays.asList(module.getClass().getDeclaredFields()));
            classFields.forEach(classField -> {
                try {
                    Config annotation = classField.getDeclaredAnnotation(Config.class);
                    if (annotation == null)
                        return;

                    // set the static property as writable so that the config can modify it
                    classField.setAccessible(true);
                    String name = annotation.name();
                    String description = annotation.description();

                    if (name.isEmpty())
                        name = classField.getName();

                    Object classValue = classField.get(null);
                    Object configValue = null;

                    if (readConfig.contains(moduleName)) {

                        // get the block of key/value pairs from the config
                        Toml moduleKeys = readConfig.getTable(moduleName);
                        Map<String, Object> mappedKeys = new HashMap<>();

                        // key names sometimes have quotes, map to remove them
                        moduleKeys.toMap().forEach((k, v) -> mappedKeys.put(k.replace("\"", ""), v));
                        configValue = mappedKeys.get(name);

                        if (configValue != null) {

                            // there's some weirdness with casting, deal with that here
                            if (classValue instanceof Integer && configValue instanceof Double)
                                configValue = (int)(double) configValue;

                            if (classValue instanceof Integer && configValue instanceof Long)
                                configValue = (int)(long) configValue;

                            classField.set(null, configValue);
                        }
                    }

                    if (configValue == null)
                        configValue = classValue;

                    // set the key/value pair. The "." specifies that it is nested
                    String moduleConfigName = moduleName + "." + name;
                    writeConfig.setComment(moduleConfigName, description);
                    writeConfig.add(moduleConfigName, configValue);

                } catch (Exception e) {
                    Charm.LOG.error("Failed to set config for " + moduleName + ": " + e.getMessage());
                }
            });
        }

        try {
            // write out and close the file
            TomlWriter tomlWriter = new TomlWriter();
            Writer buffer = Files.newBufferedWriter(path);
            tomlWriter.write(writeConfig, buffer);
            buffer.close();
            Charm.LOG.debug("Written config to disk");

        } catch (Exception e) {
            Charm.LOG.error("Failed to write config: " + e.getMessage());
        }
    }


}
