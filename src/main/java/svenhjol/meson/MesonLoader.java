package svenhjol.meson;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.*;

public abstract class MesonLoader
{
    public static Map<String, MesonLoader> instances = new HashMap<>();
    public List<Module> modules = new ArrayList<>();
    public List<Feature> features = new ArrayList<>();
    public List<Class<? extends Feature>> enabledFeatures = new ArrayList<>();
    public List<Class<? extends Module>> enabledModules = new ArrayList<>();
    public ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public ForgeConfigSpec config;
    public String id;

    public MesonLoader registerLoader(String id)
    {
        MesonLoader instance = this;
        instances.put(id, instance);
        this.id = id;
        return instance;
    }

    public void setup(Module... mods)
    {
        modules.addAll(Arrays.asList(mods));

        // configure each module
        modules.forEach(module -> {
            builder.push(module.getName());
            module.enabled = builder.define(module.getName() + " module enabled", true);
            module.setup(this);
            builder.pop();
        });

        // build config schema
        config = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config);

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
                enabledModules.add(module.getClass());
                module.init();
            }
        });
    }
}