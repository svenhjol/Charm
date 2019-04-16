package svenhjol.meson;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import svenhjol.meson.helper.ConfigHelper;

@SuppressWarnings("unused")
public abstract class Feature implements IFMLEvents
{
    protected Module module;

    public boolean enabled;
    public boolean enabledByDefault = true;
    private FeatureCompat compat;

    public void setup(Module module)
    {
        this.module = module;

        Meson.log(module.getName() + ": Adding feature " + this.getName());

        // add feature to the ModuleLoader so other things can query if the feature is available
        module.getModLoader().enabledFeatures.add(this.getClass());

        if (this.hasSubscriptions()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        if (this.hasTerrainSubscriptions()) {
            MinecraftForge.TERRAIN_GEN_BUS.register(this);
        }
        if (this.getRequiredMods().length > 0) {
            setupCompat();
        }

        setupConfig();
    }

    // setup mod compat features, override if required
    public void setupCompat()
    {
        try {
            compat = getCompatClass()
                .getConstructor(Feature.class)
                .newInstance(this);

            if (compat.hasSubscriptions()) {
                MinecraftForge.EVENT_BUS.register(compat);
            }
            if (compat.hasTerrainSubscriptions()) {
                MinecraftForge.TERRAIN_GEN_BUS.register(compat);
            }
        } catch (Exception e) {
            Meson.runtimeException(getName() + ": error loading mod-compatible feature");
        }

        Meson.log(getName() + ": setup modded feature");
    }

    public ModLoader getModLoader()
    {
        if (module == null) Meson.runtimeException("You need to setup the Feature using feature.setup()");
        return module.getModLoader();
    }

    public Configuration getConfig()
    {
        if (module == null) Meson.runtimeException("You need to setup the Feature using feature.setup()");
        return module.getConfig();
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

    public FeatureCompat getCompat()
    {
        return compat;
    }

    public Class<? extends FeatureCompat> getCompatClass()
    {
        return null;
    }

    public String getConfigCategoryName()
    {
        return module.getName() + "." + this.getName();
    }

    public int propInt(String name, String description, int def)
    {
        return ConfigHelper.propInt(getConfig(), name, getConfigCategoryName(), description, def);
    }

    public double propDouble(String name, String description, double def)
    {
        return ConfigHelper.propDouble(getConfig(), name, getConfigCategoryName(), description, def);
    }

    public boolean propBoolean(String name, String description, boolean def)
    {
        return ConfigHelper.propBoolean(getConfig(), name, getConfigCategoryName(), description, def);
    }

    public String propString(String name, String description, String def)
    {
        return ConfigHelper.propString(getConfig(), name, getConfigCategoryName(), description, def);
    }

    public String[] propStringList(String name, String description, String[] def)
    {
        return ConfigHelper.propStringList(getConfig(), name, getConfigCategoryName(), description, def);
    }
}