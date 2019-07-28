package svenhjol.meson;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;

public abstract class Feature
{
    public Module module;
    public MesonLoader modLoader;
    public ForgeConfigSpec.Builder builder;
    public ForgeConfigSpec.BooleanValue enabled;

    public void setup(Module module)
    {
        this.module = module;
        modLoader = module.modLoader;
        builder = module.builder;

        Meson.log("Configuring feature " + getName());
        configure();
    }

    public void configure()
    {
        // allow the feature to define its own configuration items
    }

    public void init()
    {
        if (hasSubscriptions()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void registerMessages()
    {
        // no op
    }

    public void registerComposterItems()
    {
        // no op
    }

    public boolean isEnabled()
    {
        return enabled.get();
    }

    public boolean isEnabledByDefault()
    {
        return true;
    }

    public boolean hasSubscriptions()
    {
        return false;
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    public String getDescription()
    {
        return getName();
    }
}
