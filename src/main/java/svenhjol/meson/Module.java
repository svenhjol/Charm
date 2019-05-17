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
    public boolean enabledByDefault = true;
    public ModLoader modLoader;
    public Configuration config;

    public void setup(ModLoader modLoader, Configuration config)
    {
        Meson.log("Adding module " + getName());
        this.modLoader = modLoader;
        this.config = config;

        modLoader.enabledModules.add(this.getClass());

        // collect enabled features here
        List<Feature> enabled = new ArrayList<>();

        features.forEach(feature -> {
            feature.enabled = ConfigHelper.propBoolean(config, feature.getName(), this.getName(), feature.getDescription(), feature.enabledByDefault);

            // only enable feature if these mods are present
            if (feature.enabled && feature.getRequiredMods().length > 0) {
                feature.enabled = ConfigHelper.checkMods(feature.getRequiredMods());
            }
            // disable the feature if these mods exist
            if (feature.enabled && feature.getDisableMods().length > 0) {
                feature.enabled = !ConfigHelper.checkMods(feature.getDisableMods());
            }

            if (feature.enabled) {
                feature.setup(this);
                enabled.add(feature);
            }
        });

        features = enabled;
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    public String getDescription()
    {
        return "";
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        features.forEach(feature -> feature.preInit(event));
    }

    public void init(FMLInitializationEvent event)
    {
        features.forEach(feature -> feature.init(event));
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        features.forEach(feature -> feature.postInit(event));
    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        features.forEach(feature -> feature.serverStarting(event));
    }
    
    @SideOnly(Side.CLIENT)
    public void preInitClient(FMLPreInitializationEvent event)
    {
        features.forEach(feature -> feature.preInitClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void initClient(FMLInitializationEvent event)
    {
        features.forEach(feature -> feature.initClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void postInitClient(FMLPostInitializationEvent event)
    {
        features.forEach(feature -> feature.postInitClient(event));
    }
}