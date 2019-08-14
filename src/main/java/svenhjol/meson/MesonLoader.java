package svenhjol.meson;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import svenhjol.meson.handler.VillagerTradingManager;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public abstract class MesonLoader
{
    public static Map<String, MesonLoader> instances = new HashMap<>();
    public IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    public List<Module> modules = new ArrayList<>();
    public List<Feature> features = new ArrayList<>();
    public Map<Class<? extends Module>, Module> enabledModules = new HashMap<>();
    public Map<Class<? extends Feature>, Feature> enabledFeatures = new HashMap<>();
    public ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public ForgeConfigSpec config;
    public String id;

    public MesonLoader(String id)
    {
        this.id = id;
        MesonLoader.instances.put(id, this);
    }

    public void add(Module... mods)
    {
        modules.addAll(Arrays.asList(mods));

        // run setup on each module
        modules.forEach(module -> {
            module.enabled = builder.define(module.getName() + " module enabled", true);
            builder.push(module.getName());
            module.setup(this);
            builder.pop();
        });

        // build config schema
        config = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, config);

        // sync config with file
        Path path = FMLPaths.CONFIGDIR.get().resolve(id + ".toml");
        final CommentedFileConfig data = CommentedFileConfig.builder(path)
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();

        data.load();
        config.setConfig(data);

        // initialize modules
        modules.forEach(module -> {
            if (module.isEnabled()) {
                enabledModules.put(module.getClass(), module);
            }
        });
        enabledModules(Module::init);
    }

    public void setup(FMLCommonSetupEvent event)
    {
        // setup feature registries
        enabledFeatures(feature -> {
            feature.registerMessages();
            feature.registerComposterItems();
        });

        VillagerTradingManager.setupTrades();
    }

    @OnlyIn(Dist.CLIENT)
    public void setupClient(FMLClientSetupEvent event)
    {
        enabledFeatures(feature -> {
            feature.initClient();
            feature.registerScreens();
        });
    }

    public void loadComplete(FMLLoadCompleteEvent event)
    {
        // no op
    }

    public void enabledModules(Consumer<Module> consumer)
    {
        enabledModules.values().forEach(consumer);
    }

    public void enabledFeatures(Consumer<Feature> consumer)
    {
        enabledFeatures.values().forEach(consumer);
    }

    public boolean hasFeature(Class<? extends Feature> feature)
    {
        return enabledFeatures.containsKey(feature);
    }

    public static void allEnabledModules(Consumer<Module> consumer)
    {
        MesonLoader.instances.values().forEach(instance -> instance.enabledModules(consumer));
    }

    public static void allEnabledFeatures(Consumer<Feature> consumer)
    {
        MesonLoader.instances.values().forEach(instance -> instance.enabledFeatures(consumer));
    }
}