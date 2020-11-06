package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;

import java.util.Collection;

@Module(mod = Charm.MOD_ID, description = "Unlocks all vanilla recipes.")
public class AutomaticRecipeUnlock extends CharmModule {
    public static void unlockRecipes(PlayerEntity player) {
        if (!ModuleHandler.enabled("charm:automatic_recipe_unlock"))
            return;

        if (player != null) {
            RecipeManager recipeManager = player.world.getRecipeManager();
            Collection<Recipe<?>> allRecipes = recipeManager.values();
            player.unlockRecipes(allRecipes);
        }
    }
}
