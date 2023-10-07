package svenhjol.charm.feature.auto_recipe_unlock;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charmony_api.event.PlayerLoginEvent;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

@Feature(
    mod = Charm.MOD_ID,
    description = "Unlocks all vanilla recipes.\nThis opinionated feature is disabled by default.",
    enabledByDefault = false
)
public class AutoRecipeUnlock extends CharmonyFeature {
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
