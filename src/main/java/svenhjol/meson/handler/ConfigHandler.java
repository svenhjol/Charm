package svenhjol.meson.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    private static final String CONFIG = "./config/charm/modules.json";
    private final Map<String, Boolean> config = new HashMap<>();

    public ConfigHandler(Meson mod) {
        if (!Meson.loadedModules.containsKey(mod.getId()))
            return;

        // build config map from all loaded modules
        Map<String, MesonModule> modules = Meson.loadedModules.get(mod.getId());

        modules.forEach((moduleName, module) -> {
            if (module.alwaysEnabled)
                return; // don't add to the config if it's a module that is always on

            this.config.put(moduleName, module.enabled);
        });

        try {
            // read module config and set module enabled values accordingly
            Map<?, ?> configMap = this.readModuleConfig();
            for (Map.Entry<?, ?> entry : configMap.entrySet()) {
                Object key = entry.getKey();
                if (!(key instanceof String)) continue;

                String moduleName = (String)key;
                boolean enabled = (boolean)entry.getValue();

                if (this.config.containsKey(moduleName)) {
                    this.config.put(moduleName, enabled);
                    modules.get(moduleName).enabled = enabled;
                }
            }

            // write out updated module enabled values
            this.writeModuleConfig(this.config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle config for " + mod.getId(), e);
        }

        // TODO config step for each module
    }

    private Map<?, ?> readModuleConfig() throws IOException {
        this.touchModuleConfig();

        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(CONFIG));
        Map<?, ?> map = gson.fromJson(reader, Map.class);
        reader.close();
        return map;
    }

    private void writeModuleConfig(Map<?, ?> map) throws IOException {
        this.touchModuleConfig();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = Files.newBufferedWriter(Paths.get(CONFIG));
        gson.toJson(map, writer);
        writer.close();
    }

    private void touchModuleConfig() throws IOException {
        File file = new File(CONFIG);

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
