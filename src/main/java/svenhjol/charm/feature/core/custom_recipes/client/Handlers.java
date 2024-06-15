package svenhjol.charm.feature.core.custom_recipes.client;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.feature.core.custom_recipes.CustomRecipesClient;
import svenhjol.charm.charmony.client.ClientRegistry;
import svenhjol.charm.charmony.feature.FeatureHolder;

import java.util.Optional;

public final class Handlers extends FeatureHolder<CustomRecipesClient> {
    public Handlers(CustomRecipesClient feature) {
        super(feature);
    }

    /**
     * Lookup a custom category for a given recipe.
     * @see svenhjol.charm.mixin.feature.core.custom_recipes.ClientRecipeBookMixin
     */
    public Optional<RecipeBookCategories> customRecipeCategory(Recipe<?> recipeHolder) {
        var getMainCategory = ClientRegistry.recipeBookMainCategory();
        RecipeType<?> recipeType = recipeHolder.getType();

        if (getMainCategory.containsKey(recipeType)) {
            return Optional.of(getMainCategory.get(recipeType));
        }

        return Optional.empty();
    }
}
