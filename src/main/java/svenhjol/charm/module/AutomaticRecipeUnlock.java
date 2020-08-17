package svenhjol.charm.module;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.network.ServerPlayerEntity;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.Collection;

@Module(description = "Unlocks all vanilla recipes.")
public class AutomaticRecipeUnlock extends MesonModule {
    public static void unlockRecipes(ServerPlayerEntity player) {
        if (!Meson.enabled("charm:automatic_recipe_unlock"))
            return;

        if (player != null) {
            RecipeManager recipeManager = player.world.getRecipeManager();
            Collection<Recipe<?>> allRecipes = recipeManager.values();
            player.unlockRecipes(allRecipes);
        }
    }
}
