package svenhjol.charm.module.automatic_recipe_unlock;

import net.minecraft.network.Connection;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.ServerJoinCallback;

import java.util.Collection;

@CommonModule(mod = Charm.MOD_ID, description = "Unlocks all vanilla recipes.")
public class AutomaticRecipeUnlock extends svenhjol.charm.loader.CommonModule {
    @Override
    public void run() {
        ServerJoinCallback.EVENT.register(this::handleServerJoin);
    }

    private void handleServerJoin(PlayerList playerManager, Connection connection, Player player) {
        if (!Charm.LOADER.isEnabled(AutomaticRecipeUnlock.class))
            return;

        if (player != null) {
            RecipeManager recipeManager = player.level.getRecipeManager();
            Collection<Recipe<?>> allRecipes = recipeManager.getRecipes();
            player.awardRecipes(allRecipes);
        }
    }
}
