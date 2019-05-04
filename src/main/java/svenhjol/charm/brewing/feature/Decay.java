package svenhjol.charm.brewing.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.brewing.potion.DecayPotion;
import svenhjol.meson.Feature;

public class Decay extends Feature
{
    public static DecayPotion decay;

    public static int duration; // how long the decay lasts in seconds
    public static int strength; // strength of wither effect

    @Override
    public String getDescription()
    {
        return "A nasty potion that withers living things.";
    }

    @Override
    public void setupConfig()
    {
        // configurable
        duration = propInt(
                "Decay duration",
                "Duration (in seconds) of decay effect when consumed.",
                5
        );
        strength = propInt(
                "Wither strength",
                "Strength of the Wither effect that accompanies the decay effect.",
                2
        );
    }

    @Override
    public String[] getDisableMods()
    {
        return new String[] { "inspirations" };
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        decay = new DecayPotion();
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
