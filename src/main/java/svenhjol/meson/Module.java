package svenhjol.meson;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Module
{
    public boolean enabled;
    public List<Feature> features = new ArrayList<>();

    public void init(FMLCommonSetupEvent event)
    {

    }
}
