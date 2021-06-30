package svenhjol.charm.handler;

import com.google.common.reflect.ClassPath;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.helper.StringHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ClientModuleHandler {
    public static ClientModuleHandler INSTANCE = new ClientModuleHandler();
    public static Map<String, CharmClientModule> MODULES = new TreeMap<>();
    private final List<Class<? extends CharmClientModule>> ENABLED_MODULES_CACHE = new ArrayList<>(); // this is a cache of enabled classes
    private final String MODULE_ANNOTATION = "Lsvenhjol/charm/annotation/ClientModule;";

    public void launch(String modId) {
        // do module registration
        prepareModules(modId).forEach((id, module) -> {
            MODULES.put(module.getName(), module);

            CharmClient.LOG.debug("Registering module " + module.getName());
            module.register();
        });

        // do module init if module is enabled
        getEnabledModules(modId).forEach(module -> {
            ENABLED_MODULES_CACHE.add(module.getClass());

            CharmClient.LOG.info("Initialising module " + module.getName());
            module.init();
        });
    }

    public List<CharmClientModule> getEnabledModules(String modId) {
        return MODULES.values().stream().filter(module
            -> module.getModule().mod.equals(modId) && module.getModule().enabled).collect(Collectors.toList());
    }

    public List<CharmClientModule> getModules(String modId) {
        return MODULES.values().stream().filter(module
            -> module.getModule().mod.equals(modId)).collect(Collectors.toList());
    }

    @Nullable
    public CharmClientModule getModule(String moduleName) {
        return MODULES.getOrDefault(StringHelper.snakeToUpperCamel(moduleName), null);
    }

    public List<Class<? extends CharmClientModule>> getEnabledModules() {
        return ENABLED_MODULES_CACHE;
    }

    public Map<String, CharmClientModule> getModules() {
        return MODULES;
    }

    private Map<String, CharmClientModule> prepareModules(String modId) {
        List<Class<? extends CharmClientModule>> discoveredClasses = new ArrayList<>();
        Map<String, CharmClientModule> discoveredModules = new LinkedHashMap<>();
        Iterable<ClassPath.ClassInfo> packageClasses;
        String basePackage = "svenhjol." + modId + ".module";

        try {
            ClassLoader classLoader = ModuleHandler.class.getClassLoader();
            packageClasses = ConfigHelper.getClassesInPackage(classLoader, basePackage);
        } catch (Exception e) {
            throw new IllegalStateException("Could not fetch client module classes, giving up: " + e.getMessage());
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
                            discoveredClasses.add((Class<? extends CharmClientModule>) Class.forName(c.getName()));
                    }
                }

                count++;

            } catch (Exception e) {
                Charm.LOG.error("> Error occurred while processing module " + truncatedName + ": " + e.getMessage());
            }
        }

        if (count == 0)
            Charm.LOG.warn("Seems no client module classes were processed... this is probably bad.");

        Map<String, CharmClientModule> loaded = new HashMap<>();
        for (Class<? extends CharmClientModule> clazz : discoveredClasses) {
            try {
                String name = clazz.getSimpleName();

                if (ModuleHandler.INSTANCE.getModules().containsKey(name)) {
                    CharmModule module = ModuleHandler.INSTANCE.getModules().get(name);
                    CharmClientModule clientModule;

                    try {
                        clientModule = clazz.getConstructor(CharmModule.class).newInstance(module);
                    } catch (Exception e) {
                        throw new RuntimeException("The chickens escaped: " + e.getMessage());
                    }

                    if (clazz.isAnnotationPresent(ClientModule.class)) {
                        ClientModule annotation = clazz.getAnnotation(ClientModule.class);

                    }

                    String moduleName = module.getName();
                    loaded.put(moduleName, clientModule);
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize client module class: " + clazz.toString(), e);
            }
        }

        // sort by module priority
        ArrayList<CharmClientModule> modList = new ArrayList<>(loaded.values());
        modList.sort((mod1, mod2) -> {
            if (mod1.priority == mod2.priority) {
                return mod1.getName().compareTo(mod2.getName()); // sort by name
            } else {
                return Integer.compare(mod2.priority, mod1.priority); // sort by priority
            }
        });

        // assemble loaded modules into discoveredModules and return
        for (CharmClientModule mod : modList) {
            for (Map.Entry<String, CharmClientModule> entry : loaded.entrySet()) {
                if (entry.getValue().equals(mod)) {
                    discoveredModules.put(entry.getKey(), mod);
                    break;
                }
            }
        }

        return discoveredModules;
    }

    /**
     * Use this within static hook methods for quick check if a module is enabled.
     * @param clazz Module to check
     * @return True if the module is enabled
     */
    public static boolean enabled(Class<? extends CharmClientModule> clazz) {
        return INSTANCE.getEnabledModules().contains(clazz);
    }
}
