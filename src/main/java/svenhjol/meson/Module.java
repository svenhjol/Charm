package svenhjol.meson;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.meson.helper.ConfigHelper;
import svenhjol.meson.iface.IFMLEvents;

import java.util.ArrayList;
import java.util.List;

public abstract class Module implements IFMLEvents
{
    public List<Feature> features = new ArrayList<>();
    public boolean enabled;
    public MesonLoader loader;
    public Configuration config;

    public void setup(MesonLoader loader, Configuration config)
    {
        this.loader = loader;
        this.config = config;
        configure();
    }

    public void configure()
    {
        features.forEach(feature -> {
            loader.features.add(feature); // add feature reference to loader
            feature.enabled = ConfigHelper.propBoolean(config, feature.getName(), this.getName(), feature.getDescription(), feature.enabledByDefault);
            feature.setup(this);
        });
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    public String getDescription()
    {
        return getName();
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        Meson.log("Starting module " + getName());
        features.stream()
            .filter(Feature::isEnabled)
            .forEach(feature -> {
                loader.enabledFeatures.add(feature.getClass());
                Meson.log("Starting feature " + feature.getName());
                feature.preInit(event);
            });
    }

    public void init(FMLInitializationEvent event)
    {
        features.stream()
            .filter(Feature::isEnabled)
            .forEach(f -> f.init(event));
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        features.stream()
            .filter(Feature::isEnabled)
            .forEach(f -> f.postInit(event));
    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        features.stream()
            .filter(Feature::isEnabled)
            .forEach(f -> f.serverStarting(event));
    }
    
    @SideOnly(Side.CLIENT)
    public void preInitClient(FMLPreInitializationEvent event)
    {
        features.stream()
            .filter(Feature::isEnabled)
            .forEach(f -> f.preInitClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void initClient(FMLInitializationEvent event)
    {
        features.stream()
            .filter(Feature::isEnabled)
            .forEach(f -> f.initClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void postInitClient(FMLPostInitializationEvent event)
    {
        features.stream()
            .filter(Feature::isEnabled)
            .forEach(f -> f.postInitClient(event));
    }
}