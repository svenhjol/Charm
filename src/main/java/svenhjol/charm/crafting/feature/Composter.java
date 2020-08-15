package svenhjol.charm.crafting.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.BlockComposter;
import svenhjol.charm.crafting.message.MessageComposterAddLevel;
import svenhjol.charm.world.compat.FutureMcBlocks;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.handler.RecipeHandler;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.registry.ProxyRegistry;

import java.util.*;

public class Composter extends Feature
{
    public static BlockComposter composter;
    public static Map<String, Float> inputs = new HashMap<>();
    public static List<String> outputs = new ArrayList<>();
    public static int maxOutput;
    public static boolean useCharmComposters;

    @Override
    public String getDescription()
    {
        return "Right-click the composter with organic items to add them.  When the composter is full, bonemeal will be returned.";
    }

    @Override
    public boolean isEnabled()
    {
        return enabled && (FutureMcBlocks.composter == null || useCharmComposters);
    }

    @Override
    public void configure()
    {
        super.configure();

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
                        "minecraft:wheat_seeds",
                        "quark:leaf_carpet",
                        "inspirations:enlightened_bush",
                        "inspirations:cactus_seeds",
                        "inspirations:sugar_cane_seeds",
                        "inspirations:carrot_seeds",
                        "inspirations:potato_seeds",
                        "futuremc:sweet_berries"
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
                        "quark:roots",
                        "quark:roots_black_flower",
                        "quark:roots_blue_flower",
                        "quark:roots_white_flower",
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
                        "minecraft:wheat",
                        "quark:root_flower",
                        "inspirations:flower",
                        "inspirations:materials[4]",
                        "inspirations:materials[5]",
                        "inspirations:edibles[0]",
                        "futuremc:cornflower",
                        "futuremc:lily_of_the_valley",
                        "futuremc:wither_rose"
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
                        "minecraft:brown_mushroom_block",
                        "quark:turf"
                }
        );
        for (String item : items) inputs.put(item, 0.85f);

        items = propStringList(
                "Items with 100% chance",
                "These items have a 100% chance of adding a level of compost.",
                new String[] {
                        "minecraft:cake",
                        "minecraft:pumpkin_pie",
                        "quark:reed_block"
                }
        );
        for (String item : items) inputs.put(item, 1.0f);

        items = propStringList(
                "Output from composter",
                "Items that may be produced by the composter when it is full.",
                new String[] {
                        "minecraft:dye[15]"
                }
        );
        outputs.addAll(Arrays.asList(items));

        maxOutput = propInt(
                "Maximum number of output items",
                "Sets the maximum stack size of the composter output.",
                3
        );

        useCharmComposters = propBoolean(
                "Use Charm composters",
                "Charm's composters will be enabled even if composters from other mods are present.",
                false
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        composter = new BlockComposter();
        GameRegistry.registerTileEntity(composter.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID + ":composter"));
        NetworkHandler.register(MessageComposterAddLevel.class, Side.CLIENT);

        if (FutureMcBlocks.composter == null) {
            RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(composter),
                "F F", "F F", "PPP",
                'F', "fenceWood",
                'P', "plankWood"
            );
        } else {
            RecipeHandler.addShapelessRecipe(ProxyRegistry.newStack(composter), new ItemStack(FutureMcBlocks.composter));
            RecipeHandler.addShapelessRecipe(new ItemStack(FutureMcBlocks.composter), ProxyRegistry.newStack(composter));
        }
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

    public static boolean hasInput(ItemStack inputStack) {
        if (inputStack.isEmpty())
            return false;

        String itemName = ItemHelper.getItemStringFromItemStack(inputStack, true);
        if ( //Check the input list for either the itemname or the itemname without meta
                Composter.inputs.containsKey(itemName)
                || Composter.inputs.containsKey(itemName.substring(0, itemName.indexOf('[')))
        ) {
            return true;
        }

        //Check for oredict entry
        for (int id : OreDictionary.getOreIDs(inputStack)) {
            if (Composter.inputs.containsKey("ore:"+OreDictionary.getOreName(id))) {
                return true;
            }
        }
        return false;
    }

    public static float getItemChance(ItemStack inputStack) {
        if (inputStack.isEmpty())
            return 0.0f;

        String itemName = ItemHelper.getItemStringFromItemStack(inputStack, true);
        if (Composter.inputs.containsKey(itemName))
            return Composter.inputs.get(itemName);

        //Check without meta
        itemName = itemName.substring(0, itemName.indexOf('['));
        if (Composter.inputs.containsKey(itemName))
            return Composter.inputs.get(itemName);

        //Check oredict
        for (int id : OreDictionary.getOreIDs(inputStack)) {
            if (Composter.inputs.containsKey("ore:"+OreDictionary.getOreName(id))) {
                return Composter.inputs.get("ore:"+OreDictionary.getOreName(id));
            }
        }
        return 0.0f;
    }
}
