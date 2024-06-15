package svenhjol.charm.feature.firing.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.charmony.Resolve;

@SuppressWarnings("unused")
public class Recipe extends AbstractCookingRecipe {
    private static final Firing FIRING = Resolve.feature(Firing.class);
    public Item icon = Items.AIR;

    public Recipe(ResourceLocation id, String group, CookingBookCategory category, Ingredient input,
                  ItemStack output, float experience, int cookTime) {
        super(FIRING.registers.recipeType.get(), id, group, category, input, output, experience, cookTime);
    }

    public Ingredient getInput() {
        return this.ingredient;
    }

    public ItemStack getRecipeKindIcon() {
        return new ItemStack(icon);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FIRING.registers.recipeSerializer.get();
    }
}
