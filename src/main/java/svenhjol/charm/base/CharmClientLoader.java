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
    private final String MOD_ID;
    private final List<Class<? extends CharmModule>> CLASSES;
    private final Map<String, CharmClientModule> LOADED_MODULES = new TreeMap<>();

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
                LOADED_MODULES.put(moduleName, client);
                ClientHandler.INSTANCE.register(client);
            }
        });
    }

    protected void init() {
        // post init, only enabled modules are run
        eachEnabledModule(client -> ClientHandler.INSTANCE.init(client));

        ClientJoinCallback.EVENT.register(client -> {
            eachEnabledModule(m -> m.loadWorld(client));
        });
    }

    protected void eachEnabledModule(Consumer<CharmClientModule> consumer) {
        LOADED_MODULES.values()
            .stream()
            .filter(m -> m.getModule().enabled)
            .forEach(consumer);
    }
}
