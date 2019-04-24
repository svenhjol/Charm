package svenhjol.charm.crafting.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.BlockComposter;
import svenhjol.charm.crafting.message.MessageComposterAddLevel;
import svenhjol.meson.Feature;
import svenhjol.meson.NetworkHandler;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.RecipeHandler;
import svenhjol.meson.helper.SoundHelper;

import java.util.*;

public class Composter extends Feature
{
    public static BlockComposter composter;
    public static Map<String, Float> inputs = new HashMap<>();
    public static List<String> outputs = new ArrayList<>();
    public static int maxOutput;

    @Override
    public String getDescription()
    {
        return "Right-click the composter with organic items to add them.  When the composter is full, bonemeal will be returned.";
    }

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
                        "minecraft:leaves2",
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
                        "minecraft:dirt[2]"
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
        composter = new BlockComposter();
        GameRegistry.registerTileEntity(composter.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID + ":composter"));
        NetworkHandler.register(MessageComposterAddLevel.class, Side.CLIENT);

        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(composter),
            "F F", "F F", "PPP",
                'F', "fenceWood",
                'P', "plankWood"
        );
    }

    @SideOnly(Side.CLIENT)
    public static void levelAdded(BlockPos pos, int level)
    {
        WorldClient world = Minecraft.getMinecraft().world;
        SoundEvent sound = level == 8 ? SoundEvents.ENTITY_ITEM_PICKUP : SoundEvents.ITEM_HOE_TILL;
        SoundHelper.playSoundAtPos(world, pos, sound, 1.0f, 1.0f);
    }

    public static Map<Float, List<String>> getInputsByChance()
    {
        Map<Float, List<String>> inputsByChance = new HashMap<>();

        // organise inputs
        for (String s : inputs.keySet()) {
            float chance = inputs.get(s);
            if (!inputsByChance.containsKey(chance)) {
                inputsByChance.put(chance, new ArrayList<>());
            }
            inputsByChance.get(chance).add(s);
        }

        return inputsByChance;
    }
}
