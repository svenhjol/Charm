package svenhjol.charm.feature.recipes;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.ClientRegistryHelper;

import java.util.Optional;

public class RecipesClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return Recipes.class;
    }

    /**
     * Lookup a custom category for a given recipe.
     * This is called by the mixin.
     * @see svenhjol.charmony.mixin.recipes.ClientRecipeBookMixin
     */
    public static Optional<RecipeBookCategories> getCustomRecipeCategory(RecipeHolder<?> recipeHolder) {
        var getMainCategory = ClientRegistryHelper.getRecipeBookMainCategory();
        RecipeType<?> recipeType = recipeHolder.value().getType();

        if (getMainCategory.containsKey(recipeType)) {
            return Optional.of(getMainCategory.get(recipeType));
        }

        return Optional.empty();
    }
}
