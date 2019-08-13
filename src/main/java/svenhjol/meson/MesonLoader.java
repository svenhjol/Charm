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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public abstract class MesonLoader
{
    public static Map<String, MesonLoader> instances = new HashMap<>();
    public List<Module> modules = new ArrayList<>();
    public List<Feature> features = new ArrayList<>();
    public Map<Class<? extends Module>, Module> enabledModules = new HashMap<>();
    public Map<Class<? extends Feature>, Feature> enabledFeatures = new HashMap<>();
    public ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public ForgeConfigSpec config;
    public String id;

    public MesonLoader register(String id)
    {
        this.id = id;
        instances.put(id, this);

        return this;
    }

    public void add(Module... mods)
    {
        modules.addAll(Arrays.asList(mods));

        // configure each module
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

        modules.forEach(module -> {
            if (module.isEnabled()) {
                enabledModules.put(module.getClass(), module);
            }
        });

        // initialize modules
        MesonLoader.forEachEnabledModule(Module::init);
    }

    public static void setup(FMLCommonSetupEvent event)
    {
        // setup feature registries
        MesonLoader.forEachEnabledFeature(feature -> {
            feature.registerMessages();
            feature.registerComposterItems();
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void setupClient(FMLClientSetupEvent event)
    {
        MesonLoader.forEachEnabledFeature(feature -> {
            feature.initClient();
            feature.registerScreens();
        });
    }

    public static void forEachEnabledModule(Consumer<Module> consumer)
    {
        instances.forEach((id, instance) -> instance.enabledModules.values().forEach(consumer));
    }

    public static void forEachEnabledFeature(Consumer<Feature> consumer)
    {
        instances.forEach((id, instance) -> instance.enabledFeatures.values().forEach(consumer));
    }

    public static boolean hasFeature(String id, Class<? extends Feature> feature)
    {
        return instances.get(id).enabledFeatures.containsKey(feature);
    }

    public static IEventBus getEventBus()
    {
        return FMLJavaModLoadingContext.get().getModEventBus();
    }
}