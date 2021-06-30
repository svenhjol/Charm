package svenhjol.charm.handler;

import com.google.common.reflect.ClassPath;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.helper.StringHelper;
import svenhjol.charm.mixin.CharmMixinConfigPlugin;
import svenhjol.charm.module.CharmModule;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ModuleHandler {
    public static ModuleHandler INSTANCE = new ModuleHandler();
    private final Map<String, CharmModule> MODULES = new TreeMap<>();
    private final List<Class<? extends CharmModule>> ENABLED_MODULES_CACHE = new ArrayList<>();
    private static final String MODULE_ANNOTATION = "Lsvenhjol/charm/annotation/Module;";

    public List<CharmModule> getEnabledModules(String modId) {
        return MODULES.values().stream().filter(module
            -> module.mod.equals(modId) && module.enabled).collect(Collectors.toList());
    }

    public List<CharmModule> getModules(String modId) {
        return MODULES.values().stream().filter(module
            -> module.mod.equals(modId)).collect(Collectors.toList());
    }

    public List<Class<? extends CharmModule>> getEnabledModules() {
        return ENABLED_MODULES_CACHE;
    }

    public Map<String, CharmModule> getModules() {
        return MODULES;
    }

    @Nullable
    public CharmModule getModule(String moduleName) {
        return MODULES.getOrDefault(StringHelper.snakeToUpperCamel(moduleName), null);
    }

    public void launch(String modId) {
        // do module registration and dependency check
        prepareModules(modId).forEach((id, module) -> {
            MODULES.put(module.getName(), module);

            Charm.LOG.debug("Registering module " + module.getName());
            module.register();
        });

        // do module dependency check
        getModules(modId).forEach(module -> {
            String name = module.getName();
            boolean isEnabled = module.enabled;
            boolean passedDependencyCheck = module.depends();
            module.enabled = isEnabled && passedDependencyCheck;

            if (!isEnabled) {
                Charm.LOG.debug("> Module " + name + " is disabled");
            } else if (!passedDependencyCheck) {
                Charm.LOG.debug("> Module " + name + " did not pass dependency check");
            } else {
                Charm.LOG.debug("> Module " + name + " is enabled ");
            }
        });

        // do module init, add to enabled module cache
        getEnabledModules(modId).forEach(module -> {
            ENABLED_MODULES_CACHE.add(module.getClass());

            Charm.LOG.info("Initialising module " + module.getName());
            module.init();
        });
    }

    private Map<String, CharmModule> prepareModules(String modId) {
        List<Class<? extends CharmModule>> discoveredClasses = new ArrayList<>();
        Map<String, CharmModule> discoveredModules = new LinkedHashMap<>();
        Iterable<ClassPath.ClassInfo> packageClasses;
        String basePackage = "svenhjol." + modId + ".module";

        try {
            ClassLoader classLoader = ModuleHandler.class.getClassLoader();
            packageClasses = ConfigHelper.getClassesInPackage(classLoader, basePackage);
        } catch (Exception e) {
            throw new IllegalStateException("Could not fetch module classes, giving up: " + e.getMessage());
        }


        int count = 0;

        for (ClassPath.ClassInfo c : packageClasses) {
            String className = c.getName();
            String truncatedName = className.substring(basePackage.length() + 1);
            try {
                ClassReader classReader = new ClassReader(c.asByteSource().read());
                ClassNode node = new ClassNode();
                classReader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                if (node.visibleAnnotations != null && !node.visibleAnnotations.isEmpty()) {
                    for (AnnotationNode annotation : node.visibleAnnotations) {
                        if (annotation.desc.equals(MODULE_ANNOTATION))
                            discoveredClasses.add((Class<? extends CharmModule>) Class.forName(c.getName()));
                    }
                }

                count++;

            } catch (Exception e) {
                Charm.LOG.error("> Error occurred while processing module " + truncatedName + ": " + e.getMessage());
            }
        }

        if (count == 0)
            Charm.LOG.warn("Seems no module classes were processed... this is probably bad.");

        Map<String, CharmModule> loaded = new HashMap<>();
        for (Class<? extends CharmModule> clazz : discoveredClasses) {
            try {
                CharmModule module = clazz.getDeclaredConstructor().newInstance();
                if (clazz.isAnnotationPresent(Module.class)) {
                    Module annotation = clazz.getAnnotation(Module.class);
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
                    throw new RuntimeException("No module annotation for class " + clazz);
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize module class: " + clazz.toString(), e);
            }
        }

        // config for this module set
        ConfigHelper.createConfig(modId, loaded);

        // sort by module priority
        ArrayList<CharmModule> modList = new ArrayList<>(loaded.values());
        modList.sort((mod1, mod2) -> {
            if (mod1.priority == mod2.priority) {
                return mod1.getName().compareTo(mod2.getName()); // sort by name
            } else {
                return Integer.compare(mod2.priority, mod1.priority); // sort by priority
            }
        });

        // assemble loaded modules into discoveredModules and return
        for (CharmModule mod : modList) {
            for (Map.Entry<String, CharmModule> entry : loaded.entrySet()) {
                if (entry.getValue().equals(mod)) {
                    discoveredModules.put(entry.getKey(), mod);
                    break;
                }
            }
        }

        return discoveredModules;
    }

    private boolean checkMixins(CharmModule module) {
        for (String mixin : module.requiresMixins) {
            boolean mixinDisabled = CharmMixinConfigPlugin.isMixinDisabled(mixin);
            boolean moduleDisabled = CharmMixinConfigPlugin.isMixinDisabled(module.getId().getPath());
            if (mixinDisabled || moduleDisabled)
                return false;
        }

        return true;
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
