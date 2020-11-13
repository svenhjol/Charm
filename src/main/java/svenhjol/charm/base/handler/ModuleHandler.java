package svenhjol.charm.base.handler;

import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.StringHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.LoadWorldCallback;
import svenhjol.charm.event.StructureSetupCallback;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ModuleHandler {
    public static Map<String, List<Class<? extends CharmModule>>> AVAILABLE_MODULES = new HashMap<>();
    public static Map<String, CharmModule> LOADED_MODULES = new TreeMap<>();

    private static boolean hasInit = false;

    public static void init() {
        if (hasInit)
            return;

        instantiateModules();

        // both-side initializers
        BiomeHandler.init();

        // early init, always run, use for registering things
        eachModule(CharmModule::register);

        // post init, only enabled modules are run
        eachEnabledModule(CharmModule::init);

        // allow modules to modify structures via an event
        StructureSetupCallback.EVENT.invoker().interact();

        // listen for server world loading events
        LoadWorldCallback.EVENT.register(server -> {
            DecorationHandler.init(); // load late so that tags are populated at this point
            eachEnabledModule(m -> m.loadWorld(server));
        });

        /** @deprecated listen for server setup events (dedicated server only) */
        //DedicatedServerSetupCallback.EVENT.register(server -> {
        //    eachEnabledModule(m -> m.dedicatedServerInit(server));
        //});

        hasInit = true;
    }

    @Nullable
    public static CharmModule getModule(String moduleName) {
        return LOADED_MODULES.getOrDefault(StringHelper.snakeToUpperCamel(moduleName), null);
    }

    public static Map<String, CharmModule> getLoadedModules() {
        return LOADED_MODULES;
    }

    public static boolean enabled(String moduleName) {
        String[] split = moduleName.split(":");
        String modName = split[0]; // TODO: check module is running
        String modModule = split[1];

        CharmModule module = getModule(modModule);
        return module != null && module.enabled;
    }

    private static void instantiateModules() {
        AVAILABLE_MODULES.forEach((mod, modules) -> {
            Map<String, CharmModule> loaded = new TreeMap<>();

            modules.forEach(clazz -> {
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
            ConfigHandler.createConfig(mod, loaded);

            // add loaded modules
            loaded.forEach((moduleName, module) ->
                LOADED_MODULES.put(moduleName, module));
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
