package svenhjol.meson;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.meson.helper.ConfigHelper;

import java.io.File;
import java.util.*;

public abstract class MesonLoader
{
    public List<Module> modules = new ArrayList<>();
    public List<Feature> features = new ArrayList<>();
    public List<Class<? extends Feature>> enabledFeatures = new ArrayList<>();
    public List<Class<? extends Module>> enabledModules = new ArrayList<>();
    public static Map<String, MesonLoader> instances = new HashMap<>();
    public Configuration config;
    public String id;
    public String version;

    public MesonLoader registerModLoader(String id, String version)
    {
        MesonLoader instance = this;
        instances.put(id, instance);
        this.id = id;
        this.version = version;
        return instance;
    }

    public void setup(Module... mods)
    {
        modules.addAll(Arrays.asList(mods));
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        // setup config for mod ID
        File configFile = new File(event.getModConfigurationDirectory(), id + ".cfg");
        config = new Configuration(configFile, Charm.MOD_VERSION, true);
        config.load();

        // configure each module
        modules.forEach(module -> {
            module.enabled = ConfigHelper.propBoolean(config, module.getName(), ConfigHelper.MODULES, module.getDescription(), true);
            module.setup(this, config);
        });

        ConfigHelper.saveIfChanged(config);

        // initialise enabled modules
        modules.stream()
            .filter(Module::isEnabled)
            .forEach(module -> {
                enabledModules.add(module.getClass());
                module.preInit(event);
            });
    }

    public void init(FMLInitializationEvent event)
    {
        modules.stream()
            .filter(Module::isEnabled)
            .forEach(m -> m.init(event));
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        modules.stream()
            .filter(Module::isEnabled)
            .forEach(m -> m.postInit(event));
    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        modules.stream()
            .filter(Module::isEnabled)
            .forEach(m -> m.serverStarting(event));
    }

    @SideOnly(Side.CLIENT)
    public void preInitClient(FMLPreInitializationEvent event)
    {
        modules.stream()
            .filter(Module::isEnabled)
            .forEach(m -> m.preInitClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void initClient(FMLInitializationEvent event)
    {
        modules.stream()
            .filter(Module::isEnabled)
            .forEach(m -> m.initClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void postInitClient(FMLPostInitializationEvent event)
    {
        modules.stream()
            .filter(Module::isEnabled)
            .forEach(m -> m.postInitClient(event));
    }
}
