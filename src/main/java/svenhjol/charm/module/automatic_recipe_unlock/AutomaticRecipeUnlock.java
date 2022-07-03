package svenhjol.charm.module.automatic_recipe_unlock;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

import java.util.Collection;

@CommonModule(mod = Charm.MOD_ID, description = "Unlocks all vanilla recipes.", enabledByDefault = false)
public class AutomaticRecipeUnlock extends CharmModule {
    @Override
    public void runWhenEnabled() {
        ServerEntityEvents.ENTITY_LOAD.register(this::handleServerJoin);
    }

    private void handleServerJoin(Entity entity, ServerLevel level) {
        if (!Charm.LOADER.isEnabled(AutomaticRecipeUnlock.class)) return;

        if (entity instanceof Player player) {
            RecipeManager recipeManager = player.level.getRecipeManager();
            Collection<Recipe<?>> allRecipes = recipeManager.getRecipes();
            player.awardRecipes(allRecipes);
        }
    }
}
