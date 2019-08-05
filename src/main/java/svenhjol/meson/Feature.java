package svenhjol.meson;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.meson.helper.ConfigHelper;
import svenhjol.meson.iface.IFMLEvents;

@SuppressWarnings("unused")
public abstract class Feature implements IFMLEvents
{
    public boolean enabled;
    public boolean enabledByDefault = true;
    protected Module module;
    protected MesonLoader loader;
    protected Configuration config;

    public void setup(Module module)
    {
        this.module = module;
        this.config = module.config;
        this.loader = module.loader;

        Meson.log("Configuring feature " + getName());
        configure();
    }

    public void configure()
    {
        // allow the feature to define its own configuration
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        // override to initialise the feature

        if (this.hasSubscriptions()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        if (this.hasTerrainSubscriptions()) {
            MinecraftForge.TERRAIN_GEN_BUS.register(this);
        }
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

    public boolean isEnabledByDefault()
    {
        return true;
    }
    public boolean hasSubscriptions()
    {
        return false;
    }

    public boolean hasTerrainSubscriptions()
    {
        return false;
    }

    public String getConfigCategoryName()
    {
        return module.getName() + "." + this.getName();
    }

    public int propInt(String name, String description, int def)
    {
        return ConfigHelper.propInt(config, name, getConfigCategoryName(), description + " (default " + def + ")", def);
    }

    public double propDouble(String name, String description, double def)
    {
        return ConfigHelper.propDouble(config, name, getConfigCategoryName(), description + " (default " + def + ")", def);
    }

    public boolean propBoolean(String name, String description, boolean def)
    {
        return ConfigHelper.propBoolean(config, name, getConfigCategoryName(), description + " (default " + def + ")", def);
    }

    public String propString(String name, String description, String def)
    {
        return ConfigHelper.propString(config, name, getConfigCategoryName(), description + " (default " + def + ")", def);
    }

    public String[] propStringList(String name, String description, String[] def)
    {
        return ConfigHelper.propStringList(config, name, getConfigCategoryName(), description, def);
    }
}