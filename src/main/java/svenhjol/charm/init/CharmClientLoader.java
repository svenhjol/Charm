package svenhjol.charm.init;

import svenhjol.charm.CharmClient;
import svenhjol.charm.init.CharmLoader;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ClientHandler;
import svenhjol.charm.handler.ModuleHandler;

import java.util.*;
import java.util.function.Consumer;

public class CharmClientLoader {
    private final String MOD_ID;
    private final List<Class<? extends CharmModule>> CLASSES;
    private final Map<String, CharmClientModule> LOADED_MODULES = new LinkedHashMap<>();

    public CharmClientLoader(String modId) {
        CharmLoader loader = ModuleHandler.INSTANCE.getLoader(modId);
        if (loader == null)
            throw new RuntimeException("Cannot find chicken");

        CharmClient.LOG.info("Setting up client modules for '" + modId + "'");

        MOD_ID = modId;
        CLASSES = loader.getClasses();

        register();
        init();

        CharmClient.LOG.info("Done setting up client modules for '" + modId + "'");
    }

    public String getModId() {
        return MOD_ID;
    }

    public List<Class<? extends CharmModule>> getClasses() {
        return CLASSES;
    }

    protected void register() {
        Map<String, CharmClientModule> loaded = new HashMap<>();

        CLASSES.forEach(moduleClass -> {
            String name = moduleClass.getSimpleName();

            if (ModuleHandler.LOADED_MODULES.containsKey(name)) {
                CharmModule module = ModuleHandler.LOADED_MODULES.get(name);
                CharmClientModule client;

                Class<? extends CharmClientModule> clazz = module.client;
                if (clazz == null || clazz == CharmClientModule.class)
                    return;

                try {
                    client = clazz.getConstructor(CharmModule.class).newInstance(module);
                } catch (Exception e) {
                    CharmClient.LOG.error("Failed to create the client for " + module.getName());
                    throw new RuntimeException("The chickens escaped", e);
                }

                String moduleName = module.getName();
                loaded.put(moduleName, client);
                ClientHandler.INSTANCE.register(client);
            }
        });

        // sort by module priority
        ArrayList<CharmClientModule> modList = new ArrayList<>(loaded.values());
        modList.sort((mod1, mod2) -> {
            if (mod1.getModule().priority == mod2.getModule().priority) {
                // sort by name
                return mod1.getName().compareTo(mod2.getName());
            } else {
                // sort by priority
                return Integer.compare(mod2.getModule().priority, mod1.getModule().priority);
            }
        });

        for (CharmClientModule mod : modList) {
            for (Map.Entry<String, CharmClientModule> entry : loaded.entrySet()) {
                if (entry.getValue().equals(mod)) {
                    LOADED_MODULES.put(entry.getKey(), mod);
                    break;
                }
            }
        }
    }

    protected void init() {
        // post init, only enabled modules are run
        eachEnabledModule(client -> ClientHandler.INSTANCE.init(client));
    }

    protected void eachEnabledModule(Consumer<CharmClientModule> consumer) {
        LOADED_MODULES.values()
            .stream()
            .filter(m -> m.getModule().enabled)
            .forEach(consumer);
    }
}
