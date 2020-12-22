package svenhjol.charm.base;

import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.base.handler.ClientHandler;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.event.ClientJoinCallback;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class CharmClientLoader {
    private final List<Class<? extends CharmModule>> CLASSES;
    private final Map<String, CharmClientModule> LOADED_MODULES = new TreeMap<>();

    public CharmClientLoader(String modId) {
        CharmLoader loader = ModuleHandler.INSTANCE.getLoader(modId);
        if (loader == null)
            throw new RuntimeException("Cannot find chicken");

        CharmClient.LOG.info("Setting up client modules for '" + modId + "'");

        CLASSES = loader.getClasses();

        register();
        init();

        CharmClient.LOG.info("Done setting up client modules for '" + modId + "'");
    }

    private void register() {
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
                    throw new RuntimeException("The chickens escaped");
                }

                String moduleName = module.getName();
                LOADED_MODULES.put(moduleName, client);
                ClientHandler.INSTANCE.register(client);
            }
        });
    }

    private void init() {
        // post init, only enabled modules are run
        eachEnabledModule(client -> ClientHandler.INSTANCE.init(client));

        ClientJoinCallback.EVENT.register(client -> {
            eachEnabledModule(m -> m.loadWorld(client));
        });
    }

    private void eachEnabledModule(Consumer<CharmClientModule> consumer) {
        LOADED_MODULES.values()
            .stream()
            .filter(m -> m.getModule().enabled)
            .forEach(consumer);
    }
}
