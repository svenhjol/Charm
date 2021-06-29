package svenhjol.charm.handler;

import svenhjol.charm.Charm;
import svenhjol.charm.event.LoadServerFinishCallback;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.helper.StringHelper;
import svenhjol.charm.init.CharmDecoration;
import svenhjol.charm.init.CharmLoader;
import svenhjol.charm.mixin.CharmMixinConfigPlugin;
import svenhjol.charm.module.CharmModule;

import javax.annotation.Nullable;
import java.util.*;

public class ModuleHandler {
    public static ModuleHandler INSTANCE = new ModuleHandler();
    private final Map<String, CharmModule> MODULES = new TreeMap<>();
    private final Map<String, CharmLoader> LOADERS = new HashMap<>();
    private final List<Class<? extends CharmModule>> ENABLED_MODULES = new ArrayList<>(); // this is a cache of enabled classes

    private ModuleHandler() {
        // listen for server world loading events
        LoadServerFinishCallback.EVENT.register(server -> {
            // load late so that tags are populated at this point
            CharmDecoration.init();
        });
    }

    public void addLoader(CharmLoader loader) {
        LOADERS.put(loader.getModId(), loader);
    }

    @Nullable
    public CharmLoader getLoader(String modId) {
        return LOADERS.getOrDefault(modId, null);
    }

    public List<CharmLoader> getLoaders() {
        return new ArrayList<>(LOADERS.values());
    }

    public List<Class<? extends CharmModule>> getEnabledModules() {
        return ENABLED_MODULES;
    }

    public Map<String, CharmModule> getModules() {
        return MODULES;
    }

    @Nullable
    public CharmModule getModule(String moduleName) {
        return MODULES.getOrDefault(StringHelper.snakeToUpperCamel(moduleName), null);
    }

    public void register(CharmModule module) {
        MODULES.put(module.getName(), module);

        Charm.LOG.debug("Registering module " + module.getName());
        module.register();
    }

    public void depends(CharmModule module) {
        String name = module.getName();
        boolean isEnabled = module.enabled;
        boolean dependencyCheck = module.depends() && checkMixins(module);

        if (!isEnabled) {
            Charm.LOG.debug("> Module " + name + " is disabled");
        } else if (!dependencyCheck) {
            Charm.LOG.debug("> Module " + name + " did not pass dependency check");
        } else {
            Charm.LOG.debug("> Module " + name + " is enabled ");
        }

        module.enabled = isEnabled && dependencyCheck;
    }

    public boolean checkMixins(CharmModule module) {
        for (String mixin : module.requiresMixins) {
            boolean mixinDisabled = CharmMixinConfigPlugin.isMixinDisabled(mixin);
            boolean moduleDisabled = CharmMixinConfigPlugin.isMixinDisabled(module.getId().getPath());
            if (mixinDisabled || moduleDisabled)
                return false;
        }

        return true;
    }

    public void init(CharmModule module) {
        // this is a cache for quick lookup of enabled classes
        getEnabledModules().add(module.getClass());

        Charm.LOG.info("Initialising module " + module.getName());
        module.init();
    }

    /**
     * Use this within static hook methods for quick check if a module is enabled.
     * @param clazz Module to check
     * @return True if the module is enabled
     */
    public static boolean enabled(Class<? extends CharmModule> clazz) {
        return INSTANCE.getEnabledModules().contains(clazz);
    }

    /**
     * Use this anywhere to check a module's enabled status for any Charm-based (or Quark) module.
     * @param moduleName Name (modid:module_name) of module to check
     * @return True if the module is enabled
     */
    public static boolean enabled(String moduleName) {
        String[] split = moduleName.split(":");
        String modName = split[0];
        String modModule = split[1];

        if (!ModHelper.isLoaded(modName))
            return false;

        CharmModule module = INSTANCE.getModule(modModule);
        return module != null && module.enabled;
    }
}
