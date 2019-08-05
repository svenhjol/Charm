package svenhjol.meson;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import svenhjol.meson.helper.ConfigHelper;
import svenhjol.meson.iface.IFMLEvents;

@SuppressWarnings("unused")
public abstract class Feature implements IFMLEvents
{
    public boolean enabled;
    public boolean enabledByDefault = true;
    protected Module module;
    protected MesonLoader mesonLoader;
    protected Configuration config;

    public void setup(Module module)
    {
        this.module = module;
        this.config = module.config;
        this.mesonLoader = module.mesonLoader;

        Meson.log(module.getName() + ": Adding feature " + this.getName());

        // add feature to the ModuleLoader so other things can query if the feature is available
        mesonLoader.enabledFeatures.add(this.getClass());

        if (this.hasSubscriptions()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        if (this.hasTerrainSubscriptions()) {
            MinecraftForge.TERRAIN_GEN_BUS.register(this);
        }

        setupConfig();
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    public String getDescription()
    {
        return "";
    }

    public void setupConfig()
    {
        // no op
    }

    public boolean hasSubscriptions()
    {
        return false;
    }

    public boolean hasTerrainSubscriptions()
    {
        return false;
    }

    public String[] getRequiredMods()
    {
        return new String[] {};
    }

    public String[] getDisableMods() { return new String[] {}; }

    public boolean checkSelf()
    {
        return true;
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