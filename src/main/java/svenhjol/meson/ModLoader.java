package svenhjol.meson;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ModLoader
{
    public List<Module> modules = new ArrayList<>();

    public void setup(FMLCommonSetupEvent event)
    {
        // allow the Meson-based mod to set up its configuration
        config(event);

        // create new list of enabled modules
        List<Module> enabled = new ArrayList<>();

        modules.forEach(module -> {
            // TODO module setup based on config
        });

        // set the modules to the enabled ones
        modules = enabled;

        // run init function for each module
        modules.forEach(module -> module.init(event));
    }

    public void config(FMLCommonSetupEvent event)
    {
        // no op
    }

    public void add(Module... module)
    {
        modules.addAll(Arrays.asList(module));
    }
}