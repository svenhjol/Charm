package svenhjol.meson.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    private static final String CONFIG_PATH = "./config/charm";
    private static final String MODULES = CONFIG_PATH + "/modules.json";
    private final Map<String, Boolean> enabledConfig = new HashMap<>();
    private final Map<String, Map<String, Object>> moduleConfig = new HashMap<>();

    public ConfigHandler(Meson mod) {
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
                boolean enabled = (boolean)entry.getValue();

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

        // TODO config step for each module
        modules.forEach((moduleName, module) -> {

            // read each module config
            Path moduleFile = Paths.get(CONFIG_PATH + "/modules/" + moduleName + ".json");
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
                // TODO log config read error
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
                        field.set(null, this.moduleConfig.get(moduleName).get(name));
                    } else {
                        this.moduleConfig.get(moduleName).put(name, val);
                    }
                } catch (Exception e) {
                    // TODO log config parse error
                }
            });

            // write out the module config
            try {
                if (!this.moduleConfig.get(moduleName).isEmpty())
                    writeConfig(moduleFile, this.moduleConfig.get(moduleName));

            } catch (Exception e) {
                // TODO log config write error
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
