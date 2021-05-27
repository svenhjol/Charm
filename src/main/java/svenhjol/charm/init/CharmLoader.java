package svenhjol.charm.init;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.annotation.Module;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class CharmLoader {
    private final String MOD_ID;
    private final List<Class<? extends CharmModule>> CLASSES;
    private final Map<String, CharmModule> LOADED_MODULES = new LinkedHashMap<>();
    private final String MODULE_ANNOTATION = "Lsvenhjol/charm/annotation/Module;";

    public CharmLoader(String modId) {
        MOD_ID = modId;
        CLASSES = new ArrayList<>(); // populate this with discovered classes

        Logger logger = LogManager.getLogger();
        ImmutableSet<ClassPath.ClassInfo> classes;
        String basePackage = "svenhjol." + modId + ".module";

        try {
            classes = ClassPath.from(CharmLoader.class.getClassLoader()).getTopLevelClassesRecursive(basePackage);
        } catch (Exception e) {
            throw new IllegalStateException("Could not fetch module classes, giving up");
        }

        for (ClassPath.ClassInfo c : classes) {
            try {
                String moduleClassName = c.getName();
                ClassReader classReader = new ClassReader(c.asByteSource().read());
                ClassNode node = new ClassNode();
                classReader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                if (node.visibleAnnotations != null && !node.visibleAnnotations.isEmpty()) {
                    for (AnnotationNode annotation : node.visibleAnnotations) {
                        if (annotation.desc.equals(MODULE_ANNOTATION))
                            CLASSES.add((Class<? extends CharmModule>) Class.forName(moduleClassName));
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

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
                    module.requiresMixins = Arrays.asList(annotation.requiresMixins());
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
        ConfigHelper.createConfig(MOD_ID, loaded);

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
        // run dependency check on each module
        eachModule(ModuleHandler.INSTANCE::depends);

        // init, only enabled modules are run
        eachEnabledModule(ModuleHandler.INSTANCE::init);
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
