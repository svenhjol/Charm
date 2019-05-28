package svenhjol.meson.handler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.ForgeRegistry;
import svenhjol.meson.Meson;
import svenhjol.meson.ProxyRegistry;

import java.util.*;

/**
 * Adapted from AutoRegLib RecipeHandler.
 * @link {http://www.wtfpl.net/}
 * @link {https://github.com/Vazkii/AutoRegLib/blob/master/src/main/java/vazkii/arl/recipe/RecipeHandler.java}
 */
@SuppressWarnings("unused")
public class RecipeHandler
{
    private static final List<ResourceLocation> usedNames = new ArrayList<>();

    public static void addShapedRecipe(ResourceLocation res, ItemStack output, Object... inputs)
    {
        ArrayList<String> pattern = Lists.newArrayList();
        Map<String, Ingredient> key = Maps.newHashMap();
        Iterator iterator = Arrays.asList(inputs).iterator();

        while (iterator.hasNext()) {
            Object input = iterator.next();

            if (input instanceof String) {
                String string = (String) input;

                if (string.length() > 3) {
                    throw new IllegalArgumentException("Invalid string length for recipe " + string.length());
                }
                if (pattern.size() <= 2) {
                    pattern.add(string);
                } else {
                    throw new IllegalArgumentException("Recipe has too many crafting rows!");
                }
            } else if (input instanceof Character) {
                key.put(((Character)input).toString(), asIngredient(iterator.next()));
            } else {
                throw new IllegalArgumentException("Unexpected argument of type " + input.getClass().toString());
            }
        }

        int width = pattern.get(0).length();
        int height = pattern.size();

        try
        {
            key.put(" ", Ingredient.EMPTY);
            NonNullList<Ingredient> ingredients = prepareMaterials(pattern.toArray(new String[0]), key, width, height);

            String group = Objects.requireNonNull(output.getItem().getRegistryName()).toString();
            ShapedRecipes recipe = new ShapedRecipes(group, width, height, ingredients, output);
            addRecipe(res, recipe);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void addShapedRecipe(ItemStack output, Object... inputs)
    {
        String namespace = getNamespace();
        ResourceLocation res = getUnusedResourceLocation(namespace, output);
        addShapedRecipe(res, output, inputs);
    }

    public static void addShapelessRecipe(ResourceLocation res, ItemStack output, Object... inputs)
    {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (Object input : inputs) {
            ingredients.add(asIngredient(input));
        }

        if (ingredients.isEmpty()) {
            throw new IllegalArgumentException("No ingredients for shapeless recipe!");
        } else if (ingredients.size() > 9) {
            throw new IllegalArgumentException("Too many ingredients for shapeless recipe!");
        }

        String group = Objects.requireNonNull(output.getItem().getRegistryName()).toString();
        ShapelessRecipes recipe = new ShapelessRecipes(group, output, ingredients);
        addRecipe(res, recipe);
    }

    public static void addShapelessRecipe(ItemStack output, Object... inputs)
    {
        String namespace = getNamespace();
        ResourceLocation res = getUnusedResourceLocation(namespace, output);
        addShapelessRecipe(res, output, inputs);
    }

    public static void addRecipe(ResourceLocation res, IRecipe recipe)
    {
        Meson.debug("Adding recipe " + res.toString());
        recipe.setRegistryName(res);
        ProxyRegistry.register(recipe);
        usedNames.add(res);
    }

    public static void removeRecipeByOutput(ItemStack stack)
    {
        ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
        ArrayList<IRecipe> recipes = Lists.newArrayList(registry.getValues());
        for (IRecipe recipe : recipes) {
            if (recipe.getRecipeOutput() == stack) {
                registry.remove(recipe.getRegistryName());
            }
        }
    }

    public static void removeRecipeByRegistryName(ResourceLocation name)
    {
        ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
        ArrayList<IRecipe> recipes = Lists.newArrayList(registry.getValues());
        for (IRecipe recipe : recipes) {
            if (Objects.requireNonNull(recipe.getRegistryName()).equals(name)) {
                registry.remove(name);
            }
        }
    }

    public static Ingredient asIngredient(Object object)
    {
        if (object instanceof Ingredient) {
            return (Ingredient)object;
        }
        if (object instanceof Item) {
            return Ingredient.fromItem((Item)object);
        }
        if (object instanceof Block) {
            return Ingredient.fromStacks(ProxyRegistry.newStack((Block)object));
        }
        if (object instanceof ItemStack) {
            return Ingredient.fromStacks((ItemStack)object);
        }
        if (object instanceof String) {
             return new OreIngredient((String)object);
        }
        throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().toString() + "to an Ingredient!");
    }

    private static String getNamespace() {
        return Objects.requireNonNull(Loader.instance().activeModContainer()).getModId();
    }

    private static ResourceLocation getUnusedResourceLocation(String namespace, ItemStack output)
    {
        ResourceLocation base = new ResourceLocation(namespace, Objects.requireNonNull(output.getItem().getRegistryName()).getPath());
        ResourceLocation unused = base;

        int i = 0;
        while (usedNames.contains(unused)) {
            i++;
            unused = new ResourceLocation(namespace, base.getPath() + "_" + i);
        }

        return unused;
    }

    // copy from vanilla
    private static NonNullList<Ingredient> prepareMaterials(String[] pattern, Map<String, Ingredient> key, int width, int height)
    {
        NonNullList<Ingredient> list = NonNullList.withSize(width * height, Ingredient.EMPTY);

        for(int i = 0; i < pattern.length; ++i)
            for (int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = key.get(s);
                list.set(j + width * i, ingredient);
            }

        return list;
    }
}
