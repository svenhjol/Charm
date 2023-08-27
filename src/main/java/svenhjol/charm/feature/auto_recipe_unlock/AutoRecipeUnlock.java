package svenhjol.charm.feature.auto_recipe_unlock;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm_api.event.PlayerLoginEvent;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

@Feature(
    mod = Charm.MOD_ID,
    description = "Unlocks all vanilla recipes.\nThis opinionated feature is disabled by default.",
    enabledByDefault = false
)
public class AutoRecipeUnlock extends CharmFeature {
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
