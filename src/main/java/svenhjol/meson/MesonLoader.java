package svenhjol.meson;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.objectweb.asm.Type;
import svenhjol.meson.handler.PlayerQueueHandler;
import svenhjol.meson.iface.Module;
import svenhjol.meson.loader.ConfigLoader;
import svenhjol.meson.loader.ModuleLoader;

import java.util.*;
import java.util.function.Consumer;

public abstract class MesonLoader
{
    public static Map<String, MesonLoader> instances = new HashMap<>();
    public List<MesonModule> modules = new ArrayList<>();
    public Map<String, List<MesonModule>> categories = new HashMap<>();
    public Map<String, Boolean> enabledModules = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    public String id;

    private ModuleLoader moduleLoader;
    private ConfigLoader configLoader;

    protected IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    protected IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

    public static final Type LOAD_MODULE = Type.getType(Module.class);

    public MesonLoader(String id)
    {
        this.id = id;
        instances.put(id, this);

        // run loaders
        this.moduleLoader = new ModuleLoader(this);
        this.configLoader = new ConfigLoader(this);

        earlyInit();

        // attach listeners
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::configChanged);
        modEventBus.addListener(this::loadComplete);
        forgeEventBus.addListener(this::serverAboutToStart);
        forgeEventBus.addListener(this::serverStarting);
        forgeEventBus.addListener(this::serverStarted);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(this::setupClient));

        // init every module, this registers blocks
        modules(MesonModule::init);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modules(MesonModule::initClient));
    }

    public void earlyInit()
    {
        // no op
    }

    public void setup(FMLCommonSetupEvent event)
    {
        this.configLoader.refreshConfig();

        // basic queue for player tick events
        MinecraftForge.EVENT_BUS.register(new PlayerQueueHandler());

        enabledModules(module -> {
            // if module enabled, subscribe to forge event bus
            if (module.hasSubscriptions) {
                MinecraftForge.EVENT_BUS.register(module);
            }

            // if module enabled, run its setup
            module.setup(event);
        });
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event)
    {
        enabledModules(module -> module.serverAboutToStart(event));
    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        enabledModules(module -> module.serverStarting(event));
    }

    public void serverStarted(FMLServerStartedEvent event)
    {
        enabledModules(module -> module.serverStarted(event));
    }

    public void configChanged(ModConfigEvent event)
    {
        this.configLoader.refreshConfig();
        enabledModules(module -> module.configChanged(event));
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

    public void modules(Consumer<MesonModule> consumer)
    {
        modules.forEach(consumer);
    }

    public void enabledModules(Consumer<MesonModule> consumer)
    {
        modules.forEach(module -> {
            if (!module.isEnabled()) return;
            consumer.accept(module);
        });
    }

    public boolean hasModule(String module)
    {
        return enabledModules.containsKey(module) && enabledModules.get(module);
    }

    public boolean hasModule(Class<? extends MesonModule> module)
    {
        return enabledModules.containsKey(module.getSimpleName()) && enabledModules.get(module.getSimpleName());
    }

    public static void allModules(Consumer<MesonModule> consumer)
    {
        MesonLoader.instances.values().forEach(instance -> instance.modules(consumer));
    }

    public static boolean hasModule(ResourceLocation res)
    {
        String mod = res.getNamespace();

        if (instances.containsKey(mod)) {
            MesonLoader loader = instances.get(mod);
            return loader.hasModule(res.getPath());
        }

        return false;
    }
}