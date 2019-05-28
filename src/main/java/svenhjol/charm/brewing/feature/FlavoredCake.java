package svenhjol.charm.brewing.feature;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.block.BlockFlavoredCake;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.meson.Feature;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.handler.RecipeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                "swiftness",
                "strength",
                "leaping",
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

            if (Charm.hasFeature(Composter.class)) {
                // add flavored cakes to the composter inputs
                Composter.inputs.put(Objects.requireNonNull(cake.getRegistryName()).toString(), 1.0f);
            }

            RecipeHandler.addShapelessRecipe(ProxyRegistry.newStack(cake, 1),
                ProxyRegistry.newStack(Blocks.CAKE, 1),
                cake.potionItem
            );
        }
    }
}
