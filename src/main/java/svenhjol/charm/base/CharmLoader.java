package svenhjol.charm.base;

import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ConfigHandler;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.LoadWorldCallback;

import java.util.*;
import java.util.function.Consumer;

public class CharmLoader {
    private final String MOD_ID;
    private final List<Class<? extends CharmModule>> CLASSES;
    private final Map<String, CharmModule> LOADED_MODULES = new LinkedHashMap<>();

    public CharmLoader(String modId, List<Class<? extends CharmModule>> classes) {
        MOD_ID = modId;
        CLASSES = classes;

        Charm.LOG.info("Setting up a new Charm-based module '" + modId + "'");

        ModuleHandler.INSTANCE.addLoader(this);

        register();
        init();

        Charm.LOG.info("Done setting up '" + modId + "'");
    }

    public String getModId() {
        return MOD_ID;
    }

    public List<Class<? extends CharmModule>> getClasses() {
        return CLASSES;
    }

    protected void register() {
        Map<String, CharmModule> loaded = new HashMap<>();

        CLASSES.forEach(clazz -> {
            try {
                CharmModule module = clazz.getDeclaredConstructor().newInstance();
                if (clazz.isAnnotationPresent(Module.class)) {
                    Module annotation = clazz.getAnnotation(Module.class);

                    // mod is now a required string
                    if (annotation.mod().isEmpty())
                        throw new Exception("mod name must be defined");

                    module.mod = annotation.mod();
                    module.priority = annotation.priority();
                    module.alwaysEnabled = annotation.alwaysEnabled();
                    module.enabledByDefault = annotation.enabledByDefault();
                    module.enabled = module.enabledByDefault;
                    module.description = annotation.description();
                    module.client = annotation.client();

                    String moduleName = module.getName();
                    loaded.put(moduleName, module);

                } else {
                    throw new RuntimeException("No module annotation for class " + clazz.toString());
                }

            } catch (Exception e) {
                throw new RuntimeException("Could not initialize module class: " + clazz.toString(), e);
            }
        });

        // config for this module set
        ConfigHandler.createConfig(MOD_ID, loaded);

        // sort by module priority
        ArrayList<CharmModule> modList = new ArrayList<>(loaded.values());
        modList.sort((mod1, mod2) -> {
            if (mod1.priority == mod2.priority) {
                // sort by name
                return mod1.getName().compareTo(mod2.getName());
            } else {
                // sort by priority
                return Integer.compare(mod2.priority, mod1.priority);
            }
        });

        for (CharmModule mod : modList) {
            for (Map.Entry<String, CharmModule> entry : loaded.entrySet()) {
                if (entry.getValue().equals(mod)) {
                    LOADED_MODULES.put(entry.getKey(), mod);
                    break;
                }
            }
        }

        // add and run register method for all loaded modules
        LOADED_MODULES.forEach((moduleName, module) -> ModuleHandler.INSTANCE.register(module));
    }

    protected void init() {
        // test each module's required mixins
        eachModule(ModuleHandler.INSTANCE::checkMixins);

        // run dependency check on each module
        eachModule(ModuleHandler.INSTANCE::depends);

        // init, only enabled modules are run
        eachEnabledModule(ModuleHandler.INSTANCE::init);

        // listen for server world loading events
        LoadWorldCallback.EVENT.register(server -> eachEnabledModule(m -> m.loadWorld(server)));
    }

    public void eachModule(Consumer<CharmModule> consumer) {
        LOADED_MODULES.values().forEach(consumer);
    }

    public void eachEnabledModule(Consumer<CharmModule> consumer) {
        LOADED_MODULES.values()
            .stream()
            .filter(m -> m.enabled)
            .forEach(consumer);
    }
}
