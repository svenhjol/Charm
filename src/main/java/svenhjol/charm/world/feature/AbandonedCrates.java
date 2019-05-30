package svenhjol.charm.world.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import svenhjol.charm.world.generator.AbandonedCrateGenerator;
import svenhjol.meson.Feature;

public class AbandonedCrates extends Feature
{
    public static int maxTries;
    public static int lowerLimit;
    public static int upperLimit;
    public static double generateChance;
    public static float rareChance;
    public static float valuableChance;
    public static float uncommonChance;

    @Override
    public String getDescription()
    {
        return "Abandoned Crates may be found underground.  These crates are sealed so must be smashed to obtain contained items.";
    }

    @Override
    public void setupConfig()
    {
        generateChance = propDouble(
            "Generate crate chance",
            "Chance (out of 1.0) of a crate generating in a chunk, if it is possible to do so.",
            0.3D
        );

        upperLimit = propInt(
                "Upper limit",
                "Crates will spawn lower than this Y value.",
                48
        );

        lowerLimit = propInt(
                "Lower limit",
                "Crates will spawn higher than this Y value.\n" +
                        "For Cubic Chunks you should set this value very low.",
                28
        );

        // internal
        maxTries = 50;
        rareChance = 0.005f;
        valuableChance = 0.06f;
        uncommonChance = 0.2f;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        GameRegistry.registerWorldGenerator(new AbandonedCrateGenerator(), 1000);
    }
}
