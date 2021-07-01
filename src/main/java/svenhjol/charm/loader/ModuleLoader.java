package svenhjol.charm.loader;

import svenhjol.charm.Charm;
import svenhjol.charm.helper.ClassHelper;
import svenhjol.charm.helper.StringHelper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ModuleLoader<T extends ICharmModule> {
    private final Map<String, T> MODULES = new TreeMap<>();
    private final String modId;
    private final String basePackage;

    public ModuleLoader(String modId, String basePackage) {
        this.modId = modId;
        this.basePackage = basePackage;
    }

    public void init() {
        // do module registration
        this.register();

        // do module dependency check
        this.dependencies();

        // do module run if enabled
        this.run();
    }

    protected void register() {
        prepareModules().forEach((id, module) -> {
            Charm.LOG.debug("Registering " + module.getName());
            MODULES.put(StringHelper.upperCamelToSnake(module.getName()), module);
            module.register();
        });
    }

    protected void dependencies() {
        getModules().forEach(module -> {
            boolean passed = module.getDependencies().isEmpty() || module.getDependencies().stream().allMatch(dep -> dep.test(module));

            if (!passed) {
                module.setEnabled(false);
                Charm.LOG.debug("> " + module.getName() + " did not pass dependency check");
            } else if (!module.isEnabled()) {
                Charm.LOG.debug("> " + module.getName() + " is disabled");
            } else {
                Charm.LOG.debug("> " + module.getName() + " is enabled ");
            }
        });
    }

    protected void run() {
        getEnabledModules().forEach(module -> {
            Charm.LOG.info("Initialising " + module.getName());
            module.run();
        });
    }

    /**
     * Use this anywhere to check a module's enabled status for any Charm-based module.
     */
    public boolean isEnabled(Class<? extends T> clazz) {
        return getModules().stream().anyMatch(module -> module.getClass().equals(clazz));
    }

    /**
     * Use this anywhere to check a module's enabled status for any Charm-based module.
     */
    public boolean isEnabled(String moduleName) {
        // deprecated, warn about it
        if (moduleName.contains(":")) {
            Charm.LOG.warn("moduleName no longer requires namespace: `" + moduleName + "`");
            moduleName = moduleName.split(":")[1];
        }

        T module = getModule(moduleName);
        return module != null && module.isEnabled();
    }

    public List<T> getModules() {
        return MODULES.values().stream().toList();
    }

    public List<T> getEnabledModules() {
        return MODULES.values().stream().filter(ICharmModule::isEnabled).collect(Collectors.toList());
    }

    @Nullable
    public T getModule(String moduleName) {
        String name;

        // if fully qualified name, split
        if (moduleName.contains(".")) {
            String[] split = moduleName.split("\\.");
            name = split[split.length - 1];
        } else {
            name = moduleName;
        }

        String lower = StringHelper.upperCamelToSnake(name);
        return MODULES.getOrDefault(lower, null);
    }

    protected Map<String, T> prepareModules() {
        Map<String, T> discoveredModules = new LinkedHashMap<>();
        List<Class<T>> discoveredClasses = ClassHelper.getClassesInPackage(getBasePackage(), getModuleAnnotation());

        if (discoveredClasses.isEmpty())
            Charm.LOG.warn("Seems no module classes were processed... this is probably bad.");

        Map<String, T> loaded = new HashMap<>();
        for (Class<T> clazz : discoveredClasses) {
            try {
                T module = clazz.getDeclaredConstructor().newInstance();
                setupModuleAnnotations(clazz, module);

                String moduleName = module.getName();
                loaded.put(moduleName, module);
            } catch (Exception e) {
                Charm.LOG.error("Error loading module " + clazz.toString() + ": " + e.getMessage());
            }
        }

        // defer module config to subclasses
        setupModuleConfig(loaded);

        // sort by module priority
        ArrayList<T> modList = new ArrayList<>(loaded.values());
        modList.sort((mod1, mod2) -> {
            if (mod1.getPriority() == mod2.getPriority()) {
                return mod1.getName().compareTo(mod2.getName()); // sort by name
            } else {
                return Integer.compare(mod2.getPriority(), mod1.getPriority()); // sort by priority
            }
        });

        // assemble loaded modules into discoveredModules and return
        for (T mod : modList) {
            for (Map.Entry<String, T> entry : loaded.entrySet()) {
                if (entry.getValue().equals(mod)) {
                    discoveredModules.put(entry.getKey(), mod);
                    break;
                }
            }
        }

        return discoveredModules;
    }

    protected String getModId() {
        return this.modId;
    }

    protected String getBasePackage() {
        return this.basePackage;
    }

    protected abstract String getModuleAnnotation();

    protected abstract void setupModuleAnnotations(Class<T> clazz, T module) throws IllegalStateException;

    protected void setupModuleConfig(Map<String, T> loadedModules) {
        // no op
    }
}
