package svenhjol.meson;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.objectweb.asm.Type;
import svenhjol.meson.handler.VillagerTradingManager;
import svenhjol.meson.iface.Module;
import svenhjol.meson.loader.ConfigLoader;
import svenhjol.meson.loader.ModuleLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class MesonLoader
{
    public static Map<String, MesonLoader> instances = new HashMap<>();
    public IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    public List<MesonModule> modules = new ArrayList<>();
    public Map<String, List<MesonModule>> categories = new HashMap<>();
    public Map<Class<? extends MesonModule>, MesonModule> enabledModules = new HashMap<>();
    public String id;

    private ModuleLoader moduleLoader;
    private ConfigLoader configLoader;

    public static final Type LOAD_MODULE = Type.getType(Module.class);

    public MesonLoader(String id)
    {
        this.id = id;
        instances.put(id, this);

        // attach listeners
        bus.addListener(this::setup);
        bus.addListener(this::configChanged);
        bus.addListener(this::loadComplete);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> bus.addListener(this::setupClient));

        // run loaders
        this.moduleLoader = new ModuleLoader(this);
        this.configLoader = new ConfigLoader(this);

        // init every module, this registers blocks
        this.modules.forEach(MesonModule::init);
    }

    public void setup(FMLCommonSetupEvent event)
    {
        enabledModules(module -> {
            if (module.hasSubscriptions) {
                MinecraftForge.EVENT_BUS.register(this);
            }
            module.setup(event);
        });

        VillagerTradingManager.setupTrades();
    }

    public void configChanged(ModConfigEvent event)
    {
        this.configLoader.configure();
    }

    @OnlyIn(Dist.CLIENT)
    public void setupClient(FMLClientSetupEvent event)
    {
        enabledModules(module -> module.setupClient(event));
    }

    public void loadComplete(FMLLoadCompleteEvent event)
    {
        enabledModules(module -> module.loadComplete(event));
    }

    public void enabledModules(Consumer<MesonModule> consumer)
    {
        enabledModules.values().forEach(consumer);
    }

    public boolean hasModule(Class<? extends MesonModule> module)
    {
        return enabledModules.containsKey(module);
    }

    public static void allEnabledModules(Consumer<MesonModule> consumer)
    {
        MesonLoader.instances.values().forEach(instance -> instance.enabledModules(consumer));
    }
}