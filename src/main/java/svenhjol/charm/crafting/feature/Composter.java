package svenhjol.charm.crafting.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.crafting.block.BlockComposter;
import svenhjol.meson.Feature;

import java.util.*;

public class Composter extends Feature
{
    public static BlockComposter block;
    public static Map<String, Float> inputs = new HashMap<>();
    public static List<String> outputs = new ArrayList<>();
    public static int maxOutput;

    @Override
    public void setupConfig()
    {
        String[] items;

        items = propStringList(
                "Items with 30% chance",
                "These items have a 30% chance of adding a level of compost.",
                new String[] {
                        "minecraft:beetroot_seeds",
                        "minecraft:grass",
                        "minecraft:leaves",
                        "minecraft:melon_seeds",
                        "minecraft:pumpkin_seeds",
                        "minecraft:sapling",
                        "minecraft:wheat_seeds"
                }
        );
        for (String item : items) inputs.put(item, 0.3f);

        items = propStringList(
                "Items with 50% chance",
                "These items have a 50% chance of adding a level of compost.",
                new String[] {
                        "minecraft:cactus",
                        "minecraft:melon",
                        "minecraft:reeds",
                        "minecraft:double_plant",
                        "minecraft:tallgrass",
                }
        );
        for (String item : items) inputs.put(item, 0.5f);

        items = propStringList(
                "Items with 65% chance",
                "These items have a 65% chance of adding a level of compost.",
                new String[] {
                        "minecraft:apple",
                        "minecraft:beetroot",
                        "minecraft:carrot",
                        "minecraft:dye[3]",
                        "minecraft:tallgrass[2]",
                        "minecraft:yellow_flower",
                        "minecraft:red_flower",
                        "minecraft:red_mushroom",
                        "minecraft:brown_mushroom",
                        "minecraft:potato",
                        "minecraft:poisonous_potato",
                        "minecraft:pumpkin",
                        "minecraft:wheat"
                }
        );
        for (String item : items) inputs.put(item, 0.65f);

        items = propStringList(
                "Items with 85% chance",
                "These items have a 85% chance of adding a level of compost.",
                new String[] {
                        "minecraft:baked_potato",
                        "minecraft:bread",
                        "minecraft:cookie",
                        "minecraft:hay_block",
                        "minecraft:red_mushroom_block",
                        "minecraft:brown_mushroom_block"
                }
        );
        for (String item : items) inputs.put(item, 0.85f);

        items = propStringList(
                "Items with 100% chance",
                "These items have a 100% chance of adding a level of compost.",
                new String[] {
                        "minecraft:cake",
                        "minecraft:pumpkin_pie"
                }
        );
        for (String item : items) inputs.put(item, 1.0f);

        items = propStringList(
                "Output from composter",
                "Items that may be produced by the composter when it is full.",
                new String[] {
                        "minecraft:dye[15]",
                }
        );
        outputs.addAll(Arrays.asList(items));

        maxOutput = propInt(
                "Maximum number of output items",
                "Sets the maximum stack size of the composter output.",
                3
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        block = new BlockComposter();
//        NetworkHandler.register(MessageComposterInteract.class, Side.CLIENT);

        /* @todo recipe */
    }
}
