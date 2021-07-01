package svenhjol.charm.handler;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.helper.StringHelper;
import svenhjol.charm.loader.CommonLoader;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class RecipeHandler {
    public static void prepareCharmModulesFilter(Map<ResourceLocation, JsonElement> recipes) {
        CommonLoader.getAllModules().values().stream().filter(m -> m.isEnabled() && !m.getRecipesToRemove().isEmpty())
            .forEach(m -> m.getRecipesToRemove().forEach(recipes::remove));
    }

    public static Iterator<Map.Entry<ResourceLocation, JsonElement>> sortAndFilterRecipes(Map<ResourceLocation, JsonElement> recipes) {
        return sortRecipes(recipes)
            .filter(RecipeHandler::filterUnloadedRecipeTypes)
            .filter(RecipeHandler::filterUnloadedCharmModules)
            .iterator();
    }

    /**
     * When crafting recipes are loaded in game, we want modded recipes to take precedence over vanilla.
     * Sort recipes so that all modded recipes are iterated before vanilla recipes.
     */
    private static Stream<Map.Entry<ResourceLocation, JsonElement>> sortRecipes(Map<ResourceLocation, JsonElement> recipes) {
        return Stream.concat(
            recipes.entrySet().stream().filter(r -> !r.getKey().getNamespace().equals("minecraft")),
            recipes.entrySet().stream().filter(r -> r.getKey().getNamespace().equals("minecraft"))
        );
    }

    /**
     * Filter out recipes from Charm modules that have been disabled in config.
     */
    private static boolean filterUnloadedCharmModules(Map.Entry<ResourceLocation, JsonElement> recipe) {
        String namespace = recipe.getKey().getNamespace();
        String path = recipe.getKey().getPath();
        if (namespace.equals("minecraft"))
            return true;

        String moduleId = StringHelper.upperCamelToSnake(path.split("/")[0]);
        ResourceLocation check = new ResourceLocation(namespace, moduleId);

        // remove recipes for disabled charm modules
        return !CommonLoader.getAllModules().containsKey(check) || CommonLoader.getAllModules().get(check).isEnabled();
    }

    /**
     * Filter out recipes where the recipe type refers to a module that isn't loaded.
     */
    private static boolean filterUnloadedRecipeTypes(Map.Entry<ResourceLocation, JsonElement> recipe) {
        ResourceLocation type = new ResourceLocation(GsonHelper.getAsString(recipe.getValue().getAsJsonObject(), "type"));
        return type.getNamespace().equals("minecraft") || ModHelper.isLoaded(type.getNamespace());
    }
}
