package svenhjol.meson.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonMod;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.StringHelper;
import svenhjol.meson.iface.Config;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ConfigHandler {
    private static final String CONFIG_PATH = "./config/charm";
    private static final String MODULES = CONFIG_PATH + "/modules.json";
    private final Map<String, Boolean> enabledConfig = new TreeMap<>();
    private final Map<String, Map<String, Object>> moduleConfig = new TreeMap<>();

    public ConfigHandler(MesonMod mod) {
        if (!Meson.loadedModules.containsKey(mod.getId()))
            return;

        // build config map from all loaded modules
        Map<String, MesonModule> modules = Meson.loadedModules.get(mod.getId());

        modules.forEach((moduleName, module) -> {
            if (module.alwaysEnabled)
                return; // don't add to the config if it's an always-enabled module

            this.enabledConfig.put(moduleName, module.enabled);
        });

        try {
            // read module config and set module enabled values accordingly
            Map<?, ?> configMap = this.readConfig(Paths.get(MODULES));
            for (Map.Entry<?, ?> entry : configMap.entrySet()) {
                Object key = entry.getKey();
                if (!(key instanceof String)) continue;

                String moduleName = (String)key;
                boolean enabled = (Boolean)entry.getValue();

                if (this.enabledConfig.containsKey(moduleName)) {
                    this.enabledConfig.put(moduleName, enabled);
                    modules.get(moduleName).enabled = enabled;
                }
            }

            // write out updated module enabled values
            this.writeConfig(Paths.get(MODULES), this.enabledConfig);
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle config for " + mod.getId(), e);
        }

        modules.forEach((moduleName, module) -> {
            String fileName = StringHelper.upperCamelToSnake(moduleName);

            // read each module config
            Path moduleFile = Paths.get(CONFIG_PATH + "/modules/" + fileName + ".json");
            Map<?, ?> configMap = new HashMap<>();
            this.moduleConfig.put(moduleName, new HashMap<>());

            try {
                if (moduleFile.toFile().exists())
                    configMap = readConfig(moduleFile);

                for (Map.Entry<?, ?> entry : configMap.entrySet()) {
                    Object key = entry.getKey();
                    if (!(key instanceof String)) continue;

                    String optionName = (String) key;
                    Object optionVal = entry.getValue();

                    this.moduleConfig.get(moduleName).put(optionName, optionVal);
                }
            } catch (Exception e) {
                Meson.LOG.error("Failed to read module config for " + moduleName + ": " + e.getMessage());
            }

            // get annotations and set config fields
            ArrayList<Field> fields = new ArrayList<>(Arrays.asList(module.getClass().getDeclaredFields()));

            fields.forEach(field -> {
                try {
                    Config annotation = field.getDeclaredAnnotation(Config.class);
                    if (annotation == null)
                        return;

                    field.setAccessible(true);
                    String name = annotation.name();

                    if (name.isEmpty())
                        name = field.getName();

                    Object val = field.get(null);

                    if (this.moduleConfig.get(moduleName).containsKey(name)) {
                        Object configVal = this.moduleConfig.get(moduleName).get(name);

                        if (val instanceof Integer && configVal instanceof Double)
                            configVal = (int)(double)configVal;  // this is stupidland

                        field.set(null, configVal);
                        this.moduleConfig.get(moduleName).put(name, configVal);
                    } else {
                        this.moduleConfig.get(moduleName).put(name, val);
                    }
                } catch (Exception e) {
                    Meson.LOG.error("Failed to set config for " + moduleName + ": " + e.getMessage());
                }
            });

            // write out the module config
            try {
                if (!this.moduleConfig.get(moduleName).isEmpty())
                    writeConfig(moduleFile, this.moduleConfig.get(moduleName));

            } catch (Exception e) {
                Meson.LOG.error("Failed to write module config for " + moduleName + ": " + e.getMessage());
            }
        });
    }

    private Map<?, ?> readConfig(Path path) throws IOException {
        this.touch(path);

        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(path);
        Map<?, ?> map = gson.fromJson(reader, Map.class);
        reader.close();
        return map;
    }

    private void writeConfig(Path path, Map<?, ?> map) throws IOException {
        this.touch(path);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = Files.newBufferedWriter(path);
        gson.toJson(map, writer);
        writer.close();
    }

    private void touch(Path path) throws IOException {
        File file = path.toFile();

        if (file.exists())
            return;

        File dir = file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Could not create config parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new IOException("Parent file is not a directory");
        }

        try (Writer writer = new FileWriter(file)) {
            writer.write("{}");
        }
    }
}
