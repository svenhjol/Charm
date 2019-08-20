package svenhjol.meson.loader;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import svenhjol.meson.Feature;
import svenhjol.meson.MesonLoader;
import svenhjol.meson.iface.MesonConfig;

import java.lang.reflect.Field;
import java.util.*;

public class ConfigLoader
{
    private MesonLoader instance;
    private ForgeConfigSpec spec;
    private List<Runnable> configure = new ArrayList<>();

    public ConfigLoader(MesonLoader instance)
    {
        this.instance = instance;

        // build the config tree
        this.spec = new Builder().configure(this::build).getRight();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec);
    }

    public void configure()
    {
        // refresh feature config
        configure.forEach(Runnable::run);
    }

    private Void build(Builder builder)
    {
        instance.categories.entrySet().forEach(entry -> {
            String category = entry.getKey();
            List<Feature> features = entry.getValue();

            builder.push(category);
            buildCategory(builder, features);
            builder.pop();
        });

        return null;
    }

    private void buildCategory(Builder builder, List<Feature> features)
    {
        // for each feature create a config to enable/disable it
        features.forEach(feature -> {
            ForgeConfigSpec.ConfigValue<Boolean> val = builder.define( feature.getName() + " feature enabled", feature.enabledByDefault);
            configure.add(() -> {
                feature.enabled = val.get() && feature.isEnabled();
            });
        });

        // for each feature create a sublist of feature config values
        features.forEach(feature -> {
            builder.push(feature.getName());
            buildFeature(builder, feature);
            builder.pop();
        });
    }

    private void buildFeature(Builder builder, Feature feature)
    {
        // get the annotated fields
        List<Field> fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
        fields.forEach(field -> {
            MesonConfig config = field.getDeclaredAnnotation(MesonConfig.class);
            if (config != null) {
                pushConfig(builder, feature, field, config);
            }
        });
    }

    private void pushConfig(Builder builder, Feature feature, Field field, MesonConfig config)
    {
        // get the config name, fallback to the field name
        String name = config.name();
        if (name.isEmpty()) {
            name = field.getName();
        }

        // get config description and add a comment if present
        String description = config.description();
        if (!description.isEmpty()) {
            builder.comment(description);
        }

        // get config field type - TODO need to cast, probably
        Class<?> type = field.getType();

        try {
            Object value = field.get(null);
            builder.define(name, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to get config for " + feature.getName());
        }
    }
}
