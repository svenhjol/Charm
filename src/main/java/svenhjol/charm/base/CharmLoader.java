package svenhjol.charm.base;

import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ConfigHandler;
import svenhjol.charm.base.handler.DecorationHandler;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.LoadWorldCallback;

import java.util.*;
import java.util.function.Consumer;

public class CharmLoader {
    private final String MOD_ID;
    private final List<Class<? extends CharmModule>> AVAILABLE_MODULES;
    private final Map<String, CharmModule> LOADED_MODULES = new TreeMap<>();

    public CharmLoader(String modId, List<Class<? extends CharmModule>> modules) {
        MOD_ID = modId;
        AVAILABLE_MODULES = modules;

        Charm.LOG.info("Setting up a new Charm-based module '" + modId + "'");

        register();
        init();

        Charm.LOG.info("Done settting up '" + modId + "'");
    }

    protected void register() {
        Charm.LOG.info("[ModuleHandler] Registering mod: " + MOD_ID);

        Map<String, CharmModule> loaded = new TreeMap<>();

        AVAILABLE_MODULES.forEach(clazz -> {
            try {
                CharmModule module = clazz.getDeclaredConstructor().newInstance();
                if (clazz.isAnnotationPresent(Module.class)) {
                    Module annotation = clazz.getAnnotation(Module.class);

                    // mod is now a required string
                    if (annotation.mod().isEmpty())
                        throw new Exception("mod name must be defined");

                    module.mod = annotation.mod();
                    module.alwaysEnabled = annotation.alwaysEnabled();
                    module.enabledByDefault = annotation.enabledByDefault();
                    module.enabled = module.enabledByDefault;
                    module.description = annotation.description();
                    module.client = annotation.client();

                    String moduleName = module.getName();
                    LOADED_MODULES.put(moduleName, module);

                } else {
                    throw new RuntimeException("No module annotation for class " + clazz.toString());
                }

            } catch (Exception e) {
                throw new RuntimeException("Could not initialize module class: " + clazz.toString(), e);
            }
        });

        // config for this module set
        ConfigHandler.createConfig(MOD_ID, loaded);

        // add and run register method for all loaded modules
        LOADED_MODULES.forEach((moduleName, module) -> ModuleHandler.INSTANCE.register(module));
    }

    protected void init() {

        // run dependency check on each module
        eachModule(module -> ModuleHandler.INSTANCE.depends(module));

        // post init, only enabled modules are run
        eachEnabledModule(module -> ModuleHandler.INSTANCE.init(module));

        // listen for server world loading events
        LoadWorldCallback.EVENT.register(server -> {
            eachEnabledModule(m -> m.loadWorld(server));
        });
    }

    protected void eachModule(Consumer<CharmModule> consumer) {
        LOADED_MODULES.values().forEach(consumer);
    }

    protected void eachEnabledModule(Consumer<CharmModule> consumer) {
        LOADED_MODULES.values()
            .stream()
            .filter(m -> m.enabled)
            .forEach(consumer);
    }
}
