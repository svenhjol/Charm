package svenhjol.charm.module.kilns;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FiringRecipe extends AbstractCookingRecipe {
    public FiringRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(Kilns.RECIPE_TYPE, id, group, input, output, experience, cookTime);
    }

    public Ingredient getInput() {
        return this.ingredient;
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(Kilns.KILN);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Kilns.RECIPE_SERIALIZER;
    }
}
