package svenhjol.charm.brewing.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.brewing.potion.DecayPotion;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ForgeHelper;

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
    public void configure()
    {
        super.configure();

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
    public boolean isEnabled()
    {
        return !ForgeHelper.areModsLoaded("inspirations");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        decay = new DecayPotion();
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
