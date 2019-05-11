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

public class ModLoader
{
    public List<Module> modules = new ArrayList<>();
    public List<Class<? extends Feature>> enabledFeatures = new ArrayList<>();
    public List<Class<? extends Module>> enabledModules = new ArrayList<>();
    public static Map<String, ModLoader> instances = new HashMap<>();
    private Configuration config;

    public ModLoader registerModLoader(String id)
    {
        ModLoader instance = this;
        instances.put(id, instance);
        return instance;
    }

    public void add(Module... modules)
    {
        this.modules.addAll(Arrays.asList(modules));
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        // set up configuration
        File configFile = new File(event.getModConfigurationDirectory(), Charm.MOD_ID + "-1.1.cfg");

//        configFile.delete();
        config = new Configuration(configFile, Charm.MOD_VERSION, true);
        config.load();

        // setup all enabled modules
        List<Module> enabled = new ArrayList<>();

        modules.forEach(module -> {
            module.enabled = ConfigHelper.propBoolean(config, module.getName(), ConfigHelper.MODULES, module.getDescription(), module.enabledByDefault);

            if (module.enabled) {
                module.setup(this);
                enabled.add(module);
            }
        });

        modules = enabled;

        setupConfig();

        modules.forEach(module -> module.preInit(event));
    }

    public void init(FMLInitializationEvent event)
    {
        modules.forEach(module -> module.init(event));
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        modules.forEach(module -> module.postInit(event));
    }

    public void finalInit(FMLPostInitializationEvent event)
    {
        modules.forEach(module -> module.finalInit(event));

        ConfigHelper.saveIfChanged(config);
    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        modules.forEach(module -> module.serverStarting(event));
    }

    @SideOnly(Side.CLIENT)
    public void preInitClient(FMLPreInitializationEvent event)
    {
        modules.forEach(module -> module.preInitClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void initClient(FMLInitializationEvent event)
    {
        modules.forEach(module -> module.initClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void postInitClient(FMLPostInitializationEvent event)
    {
        modules.forEach(module -> module.postInitClient(event));
    }

    public Configuration getConfig()
    {
        if (config == null) Meson.runtimeException("Invalid ModLoader");
        return config;
    }

    protected void setupConfig()
    {
        // book for custom loaders to do interesting things after config is loaded and before modules init
    }
}
