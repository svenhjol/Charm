package svenhjol.charm.feature.auto_recipe_unlock;

import net.minecraft.world.entity.player.Player;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.event.PlayerLoginEvent;

public class AutoRecipeUnlock extends CommonFeature {
    @Override
    public String description() {
        return """
            Unlocks all vanilla recipes.
            This opinionated feature is disabled by default.""";
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public void runWhenEnabled() {
        PlayerLoginEvent.INSTANCE.handle(this::handlePlayerLogin);
    }

    private void handlePlayerLogin(Player player) {
        var recipeManager = player.level().getRecipeManager();
        var allRecipes = recipeManager.getRecipes();
        player.awardRecipes(allRecipes);
    }
}
