package svenhjol.charm.feature.core.custom_recipes.client;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.feature.core.custom_recipes.CustomRecipesClient;
import svenhjol.charm.foundation.client.ClientRegistry;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.Optional;

public final class Handlers extends FeatureHolder<CustomRecipesClient> {
    public Handlers(CustomRecipesClient feature) {
        super(feature);
    }

    /**
     * Lookup a custom category for a given recipe.
     * @see svenhjol.charm.mixin.feature.core.custom_recipes.ClientRecipeBookMixin
     */
    public Optional<RecipeBookCategories> customRecipeCategory(RecipeHolder<?> recipeHolder) {
        var getMainCategory = ClientRegistry.recipeBookMainCategory();
        RecipeType<?> recipeType = recipeHolder.value().getType();

        if (getMainCategory.containsKey(recipeType)) {
            return Optional.of(getMainCategory.get(recipeType));
        }

        return Optional.empty();
    }
}
