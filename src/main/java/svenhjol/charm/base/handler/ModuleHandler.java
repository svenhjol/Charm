package svenhjol.charm.base.handler;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.StringHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.LoadWorldCallback;
import svenhjol.charm.event.StructureSetupCallback;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class ModuleHandler {
    public static Map<String, CharmModule> LOADED_MODULES = new TreeMap<>();
    public static final Map<String, List<Class<? extends CharmModule>>> AVAILABLE_MODULES = new HashMap<>();
    private static final List<Class<? extends CharmModule>> ENABLED_MODULES = new ArrayList<>(); // this is a cache of enabled classes

    private boolean hasInit = false;

    public static ModuleHandler INSTANCE = new ModuleHandler();

    private ModuleHandler() {
        // both-side initializers
        BiomeHandler.init();
    }

    public void init() {
        if (hasInit)
            return;

        eachModule(module -> module.enabled = module.depends());

        // post init, only enabled modules are run
        eachEnabledModule(module -> {
            // this is a cache for quick lookup of enabled classes
            ENABLED_MODULES.add(module.getClass());
            module.init();
        });

        // allow modules to modify structures via an event
        StructureSetupCallback.EVENT.invoker().interact();

        // listen for server world loading events
        LoadWorldCallback.EVENT.register(server -> {
            DecorationHandler.init(); // load late so that tags are populated at this point
            eachEnabledModule(m -> m.loadWorld(server));
        });

        hasInit = true;
    }

    public void registerFabricMod(String modId, List<Class<? extends CharmModule>> modules) {
        AVAILABLE_MODULES.put(modId, modules);

        // create all charm-based modules
        instantiateModules(modId);
    }

    @Nullable
    public static CharmModule getModule(String moduleName) {
        return LOADED_MODULES.getOrDefault(StringHelper.snakeToUpperCamel(moduleName), null);
    }

    public static Map<String, CharmModule> getLoadedModules() {
        return LOADED_MODULES;
    }

    /**
     * Use this within static hook methods for quick check if a module is enabled.
     * @param clazz Module to check
     * @return True if the module is enabled
     */
    public static boolean enabled(Class<? extends CharmModule> clazz) {
        return ENABLED_MODULES.contains(clazz);
    }

    /**
     * Use this anywhere to check a module's enabled status for any Charm-based (or Quark) module.
     * @param moduleName Name (modid:module_name) of module to check
     * @return True if the module is enabled
     */
    public static boolean enabled(String moduleName) {
        String[] split = moduleName.split(":");
        String modName = split[0]; // TODO: check module is running
        String modModule = split[1];

        CharmModule module = getModule(modModule);
        return module != null && module.enabled;
    }

    private static void instantiateModules(String modId) {
        if (!AVAILABLE_MODULES.containsKey(modId))
            throw new RuntimeException("Could not fetch modules for " + modId + ", giving up");

        Map<String, CharmModule> loaded = new TreeMap<>();

        AVAILABLE_MODULES.get(modId).forEach(clazz -> {
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
                    loaded.put(moduleName, module);

                } else {
                    throw new RuntimeException("No module annotation for class " + clazz.toString());
                }

            } catch (Exception e) {
                throw new RuntimeException("Could not initialize module class: " + clazz.toString(), e);
            }
        });

        // config for this module set
        ConfigHandler.createConfig(modId, loaded);

        // add and run register method for all loaded modules
        loaded.forEach((moduleName, module) -> {
            LOADED_MODULES.put(moduleName, module);
            Charm.LOG.info("Loaded module " + moduleName);
            module.register();
        });
    }

    private static void eachModule(Consumer<CharmModule> consumer) {
        LOADED_MODULES.values().forEach(consumer);
    }

    private static void eachEnabledModule(Consumer<CharmModule> consumer) {
        LOADED_MODULES.values()
            .stream()
            .filter(m -> m.enabled)
            .forEach(consumer);
    }
}
