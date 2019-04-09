package svenhjol.charm.brewing.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.brewing.potion.CoffeePotion;
import svenhjol.meson.Feature;

public class Coffee extends Feature
{
    public static CoffeePotion coffee;
    public static int duration; // number of seconds of duration

    @Override
    public String getDescription()
    {
        return "Brew cocoa beans in water to make Coffee which gives you a helpful boost.";
    }

    @Override
    public void setupConfig()
    {
        duration = propInt(
            "Coffee effect duration",
            "Duration (in seconds) of Coffee effects.",
            10
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        coffee = new CoffeePotion();
    }
}