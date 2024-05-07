package svenhjol.charm.feature.firing;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

@SuppressWarnings("unused")
public class FiringRecipe extends AbstractCookingRecipe {
    public Item icon = Items.AIR;

    public FiringRecipe(String group, CookingBookCategory category, Ingredient input,
                        ItemStack output, float experience, int cookTime) {
        super(Firing.recipeType.get(), group, category, input, output, experience, cookTime);
    }

    public Ingredient getInput() {
        return this.ingredient;
    }

    public ItemStack getRecipeKindIcon() {
        return new ItemStack(icon);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Firing.recipeSerializer.get();
    }
}
