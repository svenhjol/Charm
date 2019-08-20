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
import svenhjol.meson.iface.MesonLoadModule;
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
    public List<Feature> features = new ArrayList<>();
    public Map<String, List<Feature>> categories = new HashMap<>();
    public Map<Class<? extends Feature>, Feature> enabledFeatures = new HashMap<>();
    public String id;

    private ModuleLoader moduleLoader;
    private ConfigLoader configLoader;

    public static final Type LOAD_MODULE = Type.getType(MesonLoadModule.class);

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

        // add category features to the full feature set
        categories.values().forEach(features -> {
            this.features.addAll(features);
        });

        // init all features
        this.features.forEach(Feature::init);
    }

    public void setup(FMLCommonSetupEvent event)
    {
        enabledFeatures(feature -> {
            if (feature.hasSubscriptions) {
                MinecraftForge.EVENT_BUS.register(this);
            }
            feature.setup(event);
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
        enabledFeatures(feature -> feature.setupClient(event));
    }

    public void loadComplete(FMLLoadCompleteEvent event)
    {
        enabledFeatures(feature -> feature.loadComplete(event));
    }

    public void enabledFeatures(Consumer<Feature> consumer)
    {
        enabledFeatures.values().forEach(consumer);
    }

    public boolean hasFeature(Class<? extends Feature> feature)
    {
        return enabledFeatures.containsKey(feature);
    }

    public static void allEnabledFeatures(Consumer<Feature> consumer)
    {
        MesonLoader.instances.values().forEach(instance -> instance.enabledFeatures(consumer));
    }
}