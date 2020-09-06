package svenhjol.meson.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonMod;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ConfigHandler {
    private final Map<String, Map<String, Object>> finalConfig = new LinkedHashMap<>();

    public ConfigHandler(MesonMod mod) {
        String modId = mod.getId();
        String configPath = "./config/" + modId + ".json";

        // mod must exist in Meson
        if (!Meson.loadedModules.containsKey(mod.getId()))
            return;

        // build config map from all loaded modules
        Map<String, MesonModule> modules = new TreeMap<>(Meson.loadedModules.get(modId));

        modules.forEach((moduleName, module) -> {
            this.finalConfig.put(moduleName, new LinkedHashMap<>());
            this.finalConfig.get(moduleName).put("Description", module.description);

            if (!module.alwaysEnabled)
                this.finalConfig.get(moduleName).put("Enabled", module.enabled);
        });

        // read config from disk and add to the config map
        try {
            Map<?, ?> loadedConfig = this.readConfig(Paths.get(configPath));

            for (Map.Entry<?, ?> entry : loadedConfig.entrySet()) {
                Object key = entry.getKey();
                if (!(key instanceof String))
                    continue;

                String moduleName = (String)key;
                this.finalConfig.get(moduleName).putAll((LinkedTreeMap)entry.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read config for " + modId, e);
        }

        // parse config and apply values to modules
        for (Map.Entry<String, MesonModule> entry : modules.entrySet()) {
            String moduleName = entry.getKey();
            MesonModule module = entry.getValue();

            // set module enabled/disabled
            module.enabled = (boolean)this.finalConfig.get(moduleName).getOrDefault("Enabled", module.enabledByDefault);

            // get and set module config options
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

                    if (this.finalConfig.get(moduleName).containsKey(name)) {
                        Object configVal = this.finalConfig.get(moduleName).get(name);

                        if (val instanceof Integer && configVal instanceof Double)
                            configVal = (int)(double)configVal;  // this is stupidland

                        field.set(null, configVal);
                        this.finalConfig.get(moduleName).put(name, configVal);
                    } else {
                        this.finalConfig.get(moduleName).put(name, val);
                    }
                } catch (Exception e) {
                    Meson.LOG.error("Failed to set config for " + moduleName + ": " + e.getMessage());
                }
            });
        }

        // write out the config
        try {
            this.writeConfig(Paths.get(configPath), this.finalConfig);
        } catch (Exception e) {
            Meson.LOG.error("Failed to write config: " + e.getMessage());
        }
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
