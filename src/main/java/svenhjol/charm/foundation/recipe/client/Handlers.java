package svenhjol.charm.foundation.recipe.client;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.foundation.client.ClientRegistry;

import java.util.Optional;

public final class Handlers {
    /**
     * Lookup a custom category for a given recipe.
     * @see svenhjol.charm.mixin.registry.recipes.ClientRecipeBookMixin
     */
    public static Optional<RecipeBookCategories> getCustomRecipeCategory(RecipeHolder<?> recipeHolder) {
        var getMainCategory = ClientRegistry.recipeBookMainCategory();
        RecipeType<?> recipeType = recipeHolder.value().getType();

        if (getMainCategory.containsKey(recipeType)) {
            return Optional.of(getMainCategory.get(recipeType));
        }

        return Optional.empty();
    }
}
