package svenhjol.charm.module.automatic_recipe_unlock;

import svenhjol.charm.Charm;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.event.ServerJoinCallback;

import java.util.Collection;
import net.minecraft.network.Connection;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

@Module(mod = Charm.MOD_ID, description = "Unlocks all vanilla recipes.",
    requiresMixins = "ServerJoinCallback")
public class AutomaticRecipeUnlock extends CharmModule {
    @Override
    public void init() {
        ServerJoinCallback.EVENT.register(this::handleServerJoin);
    }

    private void handleServerJoin(PlayerList playerManager, Connection connection, Player player) {
        if (!ModuleHandler.enabled("charm:automatic_recipe_unlock"))
            return;

        if (player != null) {
            RecipeManager recipeManager = player.level.getRecipeManager();
            Collection<Recipe<?>> allRecipes = recipeManager.getRecipes();
            player.awardRecipes(allRecipes);
        }
    }
}
