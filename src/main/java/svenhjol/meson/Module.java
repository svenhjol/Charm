package svenhjol.meson;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ForgeI18n;

import java.util.ArrayList;
import java.util.List;

public abstract class Module
{
    public ModLoader modLoader;
    public List<Feature> features = new ArrayList<>();
    public ForgeConfigSpec.BooleanValue enabled;
    public ForgeConfigSpec.Builder builder;

    public void setup(ModLoader modLoader)
    {
        this.modLoader = modLoader;
        builder = modLoader.builder;

        configure();
    }

    public void configure()
    {
        // setup config schema for each feature
        features.forEach(feature -> {
            String pattern = ForgeI18n.getPattern("feature." + feature.getName() + ".desc");
            builder.push(feature.getName()).comment(pattern);
            feature.enabled = builder.define(feature.getName() + " feature enabled", true);
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
