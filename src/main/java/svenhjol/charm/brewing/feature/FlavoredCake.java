package svenhjol.charm.brewing.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.brewing.block.BlockFlavoredCake;
import svenhjol.meson.Feature;

import java.util.ArrayList;
import java.util.List;

public class FlavoredCake extends Feature
{
    public static List<BlockFlavoredCake> cakes = new ArrayList<>();

    public static String[] validPotions; // potions that can be made into cakes
    public static int duration; // in seconds
    public static int amplifier;

    @Override
    public String getDescription()
    {
        return "Craft a Long Potion with a Cake to make a Flavored Cake that gives a potion effect after eating each slice.";
    }

    @Override
    public void setupConfig()
    {
        duration = propInt(
            "Cake effect duration",
            "Duration (in seconds) of the potion effect when eating a single slice of cake.",
            100
        );
        validPotions = propStringList(
            "Cake effect potion types",
            "List of Long Potions that can be used to make a cake.",
            new String[] {
                "speed",
                "strength",
                "jump_boost",
                "regeneration",
                "fire_resistance",
                "water_breathing",
                "invisibility",
                "night_vision"
            }
        );

        // internal
        amplifier = 1;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        for (String potionName : validPotions) {
            BlockFlavoredCake cake = new BlockFlavoredCake(potionName);
            cakes.add(cake);
        }
    }
}
