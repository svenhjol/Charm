package svenhjol.charm.tweaks.feature;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ItemHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FurnacesRecycleMore extends Feature
{
    public static Map<String, Map<ItemStack, Integer>> items = new HashMap<>();
    public static Map<ItemStack, ItemStack> conversion = new HashMap<>();

    @Override
    public String getDescription()
    {
        return "Increase the amount of nuggets returned when smelting iron and gold items with respect to their recipe and durability.";
    }

    @Override
    public void configure()
    {
        super.configure();

        String[] configItems = propStringList(
                "Recyclable items",
                "A map of items that return nuggets when smelted.\n" +
                        "Format is 'modid:inputname(->modid:outputname->ingots)'. Bracketed statement optional, overrides the type and number of ingots in recipe.",
                new String[] {
                    "minecraft:shears",
                    "minecraft:clock",
                    "minecraft:compass",
                    "minecraft:minecart",
                    "minecraft:hopper",
                    "minecraft:cauldron",
                    "minecraft:bucket",
                    "minecraft:rail->minecraft:iron_ingot->1",
                    "minecraft:detector_rail->minecraft:iron_ingot->1",
                    "minecraft:activator_rail->minecraft:iron_ingot->1",
                    "minecraft:golden_rail->minecraft:gold_ingot->1",
                    "minecraft:iron_door->minecraft:iron_ingot->2",
                    "minecraft:iron_bars->minecraft:iron_ingot->1",
                    "minecraft:iron_trapdoor",
                    "minecraft:iron_pickaxe",
                    "minecraft:iron_axe",
                    "minecraft:iron_hoe",
                    "minecraft:iron_sword",
                    "minecraft:iron_shovel",
                    "minecraft:iron_helmet",
                    "minecraft:iron_chestplate",
                    "minecraft:iron_leggings",
                    "minecraft:iron_boots",
                    "minecraft:golden_pickaxe",
                    "minecraft:golden_axe",
                    "minecraft:golden_hoe",
                    "minecraft:golden_sword",
                    "minecraft:golden_shovel",
                    "minecraft:golden_helmet",
                    "minecraft:golden_chestplate",
                    "minecraft:golden_leggings",
                    "minecraft:golden_boots",
                    "minecraft:golden_apple[0]",
                    "minecraft:chainmail_helmet->minecraft:iron_ingot->5",
                    "minecraft:chainmail_chestplate->minecraft:iron_ingot->8",
                    "minecraft:chainmail_leggings->minecraft:iron_ingot->7",
                    "minecraft:chainmail_boots->minecraft:iron_ingot->4",
                    "minecraft:iron_horse_armor->minecraft:iron_ingot->8",
                    "minecraft:golden_horse_armor->minecraft:gold_ingot->8",
                    "charm:nether_gold_deposit->minecraft:gold_ingot->1",
                });

        String[] configConversion = propStringList(
            "Ingot to nugget conversion",
            "Map of ingot items to their equivalent nugget items. Item recipes will be checked for these ingots.\n" +
                    "Format is 'modid:inputname[meta]->modid:outputname[meta]->nuggetsPerIngot'.",
            new String[] {
                "minecraft:iron_ingot->minecraft:iron_nugget->3",
                "minecraft:gold_ingot->minecraft:gold_nugget->3"
            }
        );


        for (String line : configItems) {
            String inputName;
            Map<ItemStack, Integer> output = new HashMap<>();

            if (line.contains("->")) {
                String[] split = line.split("->");
                inputName = split[0];
                output = new HashMap<ItemStack, Integer>() {{ put(ItemHelper.getItemStackFromItemString(split[1]), Integer.parseInt(split[2])); }};
            } else {
                inputName = line;
            }

            items.put(inputName, output);
        }

        for (String line : configConversion) {
            String[] split = line.split("->");
            if (split.length != 3) continue;

            ItemStack i1 = ItemHelper.getItemStackFromItemString(split[0]);
            ItemStack i2 = ItemHelper.getItemStackFromItemString(split[1]);
            if (i1 == null || i2 == null) continue;
            int i3 = Integer.parseInt(split[2]);

            i2.setCount(i3);
            conversion.put(i1, i2);
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        float xp = 1.0f;
        FurnaceRecipes instance = FurnaceRecipes.instance();
        Map<ItemStack, ItemStack> smeltingList = instance.getSmeltingList();

        for (ItemStack inputIngot : conversion.keySet()) {

            for (String in : items.keySet()) {
                List<ItemStack> inputStacks = ItemHelper.getItemStacksFromItemString(in, 32767);

                for (ItemStack inputStack : inputStacks) {
                    int maxIngots = 0;
                    ItemStack outputNugget = conversion.get(inputIngot);

                    if (!items.get(in).isEmpty()) {
                        Map.Entry<ItemStack, Integer> o = items.get(in).entrySet().iterator().next();
                        if (!ItemHelper.compareStacks(inputIngot, o.getKey())) continue;
                        maxIngots = o.getValue();
                    }

                    if (maxIngots == 0) {
                        IRecipe recipe = CraftingManager.getRecipe(Objects.requireNonNull(inputStack.getItem().getRegistryName()));
                        if (recipe == null) continue;

                        for (Ingredient ingredient : recipe.getIngredients()) {
                            maxIngots += ingredient.apply(inputIngot) ? 1 : 0;
                        }
                        if (maxIngots == 0) continue;
                    }

                    ItemStack outputStack = outputNugget.copy();
                    outputStack.setCount(maxIngots * outputNugget.getCount());

                    ItemStack existingInputStack = null;
                    for (Map.Entry<ItemStack, ItemStack> o : smeltingList.entrySet()) {
                        if (ItemHelper.compareStacks(inputStack, o.getKey())) {
                            existingInputStack = o.getKey();
                            break;
                        }
                    }

                    if (existingInputStack == null) {
                        instance.addSmeltingRecipe(inputStack, outputStack, xp);
                    } else {
                        smeltingList.replace(existingInputStack, outputStack);
                    }
                }
            }
        }
    }

    public static ItemStack changeSmeltingResult(ItemStack input, ItemStack output)
    {
        // check if it's a recyclable item, then get its damage
        ResourceLocation res = Objects.requireNonNull(input.getItem().getRegistryName());
        if (FurnacesRecycleMore.items.keySet().contains(res.toString())) {
            ItemStack out = output.copy();

            int maxNuggets = out.getCount();
            int countNuggets = out.getCount();
            int maxDamage = input.getMaxDamage();
            int itemDamage = input.getItemDamage();

            if (maxDamage > 0 && itemDamage > 0) {
                double d = (maxDamage - itemDamage) / (double) maxDamage;
                countNuggets = (int) Math.floor(maxNuggets * d);
            }

            out.setCount(Math.max(1, countNuggets));
            return out;
        }

        return output;
    }
}
