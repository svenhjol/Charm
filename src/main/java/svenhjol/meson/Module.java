package svenhjol.meson;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public abstract class Module
{
    public MesonLoader modLoader;
    public List<Feature> features = new ArrayList<>();
    public ForgeConfigSpec.BooleanValue enabled;
    public ForgeConfigSpec.Builder builder;

    public void setup(MesonLoader modLoader)
    {
        this.modLoader = modLoader;
        builder = modLoader.builder;

        configure();
    }

    public void configure()
    {
        // setup config schema for each feature
        features.forEach(feature -> {
            modLoader.features.add(feature); // add feature reference to modloader
            builder.push(feature.getName()).comment(feature.getDescription());
            feature.enabled = builder.define(feature.getName() + " feature enabled", feature.isEnabledByDefault());
            feature.setup(this);
            builder.pop();
        });
    }

    public void init()
    {
        Meson.log("Starting module " + getName());
        features.forEach(feature -> {
            if (feature.isEnabled()) {
                modLoader.enabledFeatures.add(feature.getClass());
                Meson.log("Starting feature " + feature.getName());
                feature.init();
            }
        });
    }

    public boolean isEnabled()
    {
        return enabled.get();
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }
}
