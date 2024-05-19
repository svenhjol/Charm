package svenhjol.charm.feature.recipe_unlocking.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.recipe_unlocking.RecipeUnlocking;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<RecipeUnlocking> {
    public Handlers(RecipeUnlocking feature) {
        super(feature);
    }

    public void playerLogin(Player player) {
        var recipeManager = player.level().getRecipeManager();
        var allRecipes = recipeManager.getRecipes();
        player.awardRecipes(allRecipes);
    }
}
