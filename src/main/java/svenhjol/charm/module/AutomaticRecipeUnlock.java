package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.PlayerManager;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.ServerJoinCallback;

import java.util.Collection;

@Module(mod = Charm.MOD_ID, description = "Unlocks all vanilla recipes.",
    requiresMixins = "ServerJoinCallback")
public class AutomaticRecipeUnlock extends CharmModule {
    @Override
    public void init() {
        ServerJoinCallback.EVENT.register(this::handleServerJoin);
    }

    private void handleServerJoin(PlayerManager playerManager, ClientConnection connection, PlayerEntity player) {
        if (!ModuleHandler.enabled("charm:automatic_recipe_unlock"))
            return;

        if (player != null) {
            RecipeManager recipeManager = player.world.getRecipeManager();
            Collection<Recipe<?>> allRecipes = recipeManager.values();
            player.unlockRecipes(allRecipes);
        }
    }
}
