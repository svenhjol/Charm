package svenhjol.meson.loader;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonInstance;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class ModuleLoader
{
    // Module file annotation magic strings
    private static final String MOD = "mod";
    private static final String CATEGORY = "category";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String HAS_SUBSCRIPTIONS = "hasSubscriptions";
    private static final String ENABLED_BY_DEFAULT = "enabledByDefault";
    private static final String CHILD_OF = "childOf";
    private static final String CLIENT = "client";
    private static final String SERVER = "server";

    private static final Type LOAD_MODULE = Type.getType(Module.class);

    private MesonInstance instance;
    private ModConfig config;
    private List<MesonModule> modules = new ArrayList<>();
    private List<Runnable> refreshConfig = new ArrayList<>();
    private List<MesonModule> enabledModules = new ArrayList<>();
    private Map<String, MesonModule> enabledModulesByIndex = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private Map<String, List<MesonModule>> categories = new HashMap<>();

    public ModuleLoader(MesonInstance instance)
    {
        this.instance = instance;

        setupModules();
        setupConfig();
    }

    private void setupModules()
    {
        // instantiate and gather all modules into categories
        ModFileScanData result = ModList.get().getModFileById(instance.getId()).getFile().getScanResult();

        List<ModFileScanData.AnnotationData> targets = result.getAnnotations().stream()
            .filter(annotationData -> LOAD_MODULE.equals(annotationData.getAnnotationType()))
            .collect(Collectors.toList());

        targets.forEach(target -> {
            try {
                String moduleClass = target.getClassType().getClassName();
                Map<String, Object> data = target.getAnnotationData();

                if (data.containsKey(CLIENT) && !(boolean)data.get(CLIENT) && FMLEnvironment.dist == Dist.CLIENT) return;
                if (data.containsKey(SERVER) && !(boolean)data.get(SERVER) && FMLEnvironment.dist == Dist.DEDICATED_SERVER) return;

                MesonModule module = (MesonModule) Class.forName(moduleClass).newInstance();
                module.mod = (String) data.get(MOD);
                module.category = (String) data.get(CATEGORY);
                module.name = data.containsKey(NAME) ? (String) data.get(NAME) : module.getClass().getSimpleName();
                module.description = data.containsKey(DESCRIPTION) ? (String) data.get(DESCRIPTION) : "";
                module.hasSubscriptions = data.containsKey(HAS_SUBSCRIPTIONS) ? (Boolean) data.get(HAS_SUBSCRIPTIONS) : false;
                module.enabledByDefault = data.containsKey(ENABLED_BY_DEFAULT) ? (Boolean) data.get(ENABLED_BY_DEFAULT) : true;
                module.childOf = data.containsKey(CHILD_OF) ? (String) data.get(CHILD_OF) : null;

                if (!categories.containsKey(module.category)) {
                    categories.put(module.category, new ArrayList<>());
                }

                // add to category and to full module set
                categories.get(module.category).add(module);
                modules.add(module);

            } catch (ReflectiveOperationException e) {

                Meson.error("Failed to load module " + target.toString());

            }
        });
    }

    private void setupConfig()
    {
        // build the config tree
        ForgeConfigSpec spec = new ForgeConfigSpec.Builder().configure(this::build).getRight();

        // register config
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        this.config = new ModConfig(ModConfig.Type.COMMON, spec, container);
        container.addConfig(this.config);

        // actual config is loaded too late to do vanilla overrides, so parse it here
        this.earlyConfigHack();
        this.refreshConfig();
    }

    private void earlyConfigHack()
    {
        List<String> lines;

        Path path = FMLPaths.CONFIGDIR.get();
        if (path == null) {
            instance.log.warn("Could not fetch config dir path");
            return;
        }

        String name = this.config.getFileName();
        if (name == null) {
            instance.log.warn("Could not fetch mod config filename");
            return;
        }

        Path configPath = Paths.get(path.toString() + File.separator + name);
        if (Files.isRegularFile(path)) {
            instance.log.warn("Config file does not exist: " + path);
            return;
        }

        try {
            lines = Files.readAllLines(configPath);
            for (String line : lines) {
                if (!line.contains("enabled")) continue;
                for (MesonModule mod : getModules()) {
                    if (line.contains(mod.name)) {
                        if (line.contains("false")) {
                            mod.enabled = false;
                        } else if (line.contains("true")) {
                            mod.enabled = true;
                        }
                        break;
                    }
                }
            }
            instance.log.debug("Finished early loading config");
        } catch (Exception e) {
            instance.log.warn("Could not read config file: " + e);
        }
    }

    private Void build(ForgeConfigSpec.Builder builder)
    {
        getCategories().forEach((category, modules) -> {
            builder.push(category);
            buildCategory(builder, modules);
            builder.pop();
        });

        return null;
    }

    private void buildCategory(ForgeConfigSpec.Builder builder, List<MesonModule> modules)
    {
        // for each module create a config to enable/disable it
        modules.forEach(module -> {
            instance.log.info("Creating config for module " + module.getName());

            if (module.childOf != null && !module.childOf.isEmpty()) {
                refreshConfig.add(() -> {
                    MesonModule parentModule = getModule(module.childOf);
                    module.enabled = parentModule != null && parentModule.enabled;
                    addEnabledModule(module);
                });
                return;
            }

            if (!module.description.isEmpty()) builder.comment(module.description);
            ForgeConfigSpec.ConfigValue<Boolean> val = builder.define(module.getName() + " enabled", module.enabledByDefault);

            refreshConfig.add(() -> {
                Boolean configEnabled = val.get();
                module.enabled = configEnabled && module.shouldBeEnabled();
                addEnabledModule(module);
            });
        });

        // for each module create a sublist of module config values
        modules.forEach(module -> {
            builder.push(module.getName());
            buildModule(builder, module);
            builder.pop();
        });
    }

    private void buildModule(ForgeConfigSpec.Builder builder, MesonModule module)
    {
        // get the annotated fields
        List<Field> fields = new ArrayList<>(Arrays.asList(module.getClass().getDeclaredFields()));
        fields.forEach(field -> {
            Config config = field.getDeclaredAnnotation(Config.class);
            if (config != null) {
                pushConfig(builder, module, field, config);
            }
        });
    }

    private void pushConfig(ForgeConfigSpec.Builder builder, MesonModule module, Field field, Config config)
    {
        field.setAccessible(true);

        // get the config name, fallback to the field name
        String name = config.name();
        if (name.isEmpty()) name = field.getName();

        // get config description and add a comment if present
        String description = config.description();
        if (!description.isEmpty()) builder.comment(description);

        // get config field type
//        Class<?> type = field.getType();

        try {
            ForgeConfigSpec.ConfigValue<?> value;
            Object defaultValue = field.get(null);

            if (defaultValue instanceof List) {
                value = builder.defineList(name, (List<?>) defaultValue, o -> true);
            } else {
                value = builder.define(name, defaultValue);
            }
            refreshConfig.add(() -> {
                try {
                    field.set(null, value.get());
                } catch (IllegalAccessException e) {
                    Meson.error("Could not set config value for " + module.getName());
                    throw new RuntimeException(e);
                }
            });
        } catch (ReflectiveOperationException e) {
            Meson.error("Failed to get config for " + module.getName());
        }
    }

    /**
     * Called by the Forge config reload event to reset all the module enabled/disabled flags.
     */
    public void refreshConfig()
    {
        refreshConfig.forEach(Runnable::run);
    }

    public Map<String, List<MesonModule>> getCategories()
    {
        return categories;
    }

    public List<MesonModule> getModules()
    {
        return modules;
    }

    public List<MesonModule> getEnabledModules()
    {
        return enabledModules;
    }

    public boolean isModuleEnabled(String module)
    {
        return enabledModulesByIndex.containsKey(module);
    }

    public MesonModule getModule(String module)
    {
        return enabledModulesByIndex.get(module);
    }

    private void addEnabledModule(MesonModule module)
    {
        if (module.enabled && !enabledModules.contains(module)) {
            enabledModules.add(module);
            enabledModulesByIndex.put(module.getName(), module);
        } else if (!module.enabled) {
            enabledModules.remove(module);
            enabledModulesByIndex.remove(module.getName());
        }
    }
}
