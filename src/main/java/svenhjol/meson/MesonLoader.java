package svenhjol.meson;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.meson.helper.ConfigHelper;

import java.util.*;

public abstract class MesonLoader
{
    public List<Module> modules = new ArrayList<>();
    public List<Class<? extends Feature>> enabledFeatures = new ArrayList<>();
    public List<Class<? extends Module>> enabledModules = new ArrayList<>();
    public static Map<String, MesonLoader> instances = new HashMap<>();
    public Configuration config;

    public MesonLoader registerModLoader(String id)
    {
        MesonLoader instance = this;
        instances.put(id, instance);
        return instance;
    }

    public void add(Module... modules)
    {
        this.modules.addAll(Arrays.asList(modules));
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        // set up this mod's config
        config = setupConfig(event);

        // setup all enabled modules
        List<Module> enabled = new ArrayList<>();

        modules.forEach(module -> {
            module.enabled = ConfigHelper.propBoolean(config, module.getName(), ConfigHelper.MODULES, module.getDescription(), module.enabledByDefault);

            if (module.enabled) {
                module.setup(this, config);
                enabled.add(module);
            }
        });

        modules = enabled;

        modules.forEach(module -> module.preInit(event));

        ConfigHelper.saveIfChanged(config);
    }

    public void init(FMLInitializationEvent event)
    {
        modules.forEach(module -> module.init(event));
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        modules.forEach(module -> module.postInit(event));
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

    protected abstract Configuration setupConfig(FMLPreInitializationEvent event);
}
