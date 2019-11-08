package svenhjol.meson.loader;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonLoader;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.loader.condition.ModuleEnabledCondition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigLoader
{
    private MesonLoader instance;
    private ForgeConfigSpec spec;
    private List<Runnable> refreshConfig = new ArrayList<>();
    private ModuleEnabledCondition.Serializer modEnabledCondition;

    public ConfigLoader(MesonLoader instance)
    {
        this.instance = instance;

        // register crafting recipe conditions
        modEnabledCondition = new ModuleEnabledCondition.Serializer(instance);
        CraftingHelper.register(modEnabledCondition);
//        CraftingHelper.register(new ResourceLocation(instance.id, "module_enabled"), json -> () -> isEnabled(JSONUtils.getString(json, "module")));

        // build the config tree
        this.spec = new Builder().configure(this::build).getRight();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec);
    }

    /**
     * Called by the Forge config reload event to reset all the module enabled/disabled flags.
     */
    public void refreshConfig()
    {
        refreshConfig.forEach(Runnable::run);
    }

    private Void build(Builder builder)
    {
        instance.categories.entrySet().forEach(entry -> {
            String category = entry.getKey();
            List<MesonModule> modules = entry.getValue();

            builder.push(category);
            buildCategory(builder, modules);
            builder.pop();
        });

        return null;
    }

    private void buildCategory(Builder builder, List<MesonModule> modules)
    {
        // for each module create a config to enable/disable it
        modules.forEach(module -> {
            Meson.log("Creating config for module " + module.getName());
            if (!module.description.isEmpty()) builder.comment(module.description);
            ForgeConfigSpec.ConfigValue<Boolean> val = builder.define(module.getName() + " enabled", module.enabledByDefault);

            refreshConfig.add(() -> {
                module.enabled = val.get() && module.isEnabled();
                instance.enabledModules.put(module.name, module.enabled);
            });
        });

        // for each module create a sublist of module config values
        modules.forEach(module -> {
            builder.push(module.getName());
            buildModule(builder, module);
            builder.pop();
        });
    }

    private void buildModule(Builder builder, MesonModule module)
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

    private void pushConfig(Builder builder, MesonModule module, Field field, Config config)
    {
        field.setAccessible(true);

        // get the config name, fallback to the field name
        String name = config.name();
        if (name.isEmpty()) name = field.getName();

        // get config description and add a comment if present
        String description = config.description();
        if (!description.isEmpty()) builder.comment(description);

        // get config field type - TODO need to cast, probably
        Class<?> type = field.getType();

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
                    throw new RuntimeException(e);
                }
            });
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to get config for " + module.getName());
        }
    }
}
