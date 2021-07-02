package svenhjol.charm.helper;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import svenhjol.charm.loader.CommonLoader;
import svenhjol.charm.module.core.Core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RecipeHelper {
    private static final List<ResourceLocation> RECIPES_TO_REMOVE = new ArrayList<>();

    public static void removeRecipe(ResourceLocation id) {
        Core.debug("[RecipeHelper] Added `" + id + "` to list of recipes to remove from the game");
        RECIPES_TO_REMOVE.add(id);
    }

    public static void prepareCharmModulesFilter(Map<ResourceLocation, JsonElement> recipes) {
        RECIPES_TO_REMOVE.stream().distinct().forEach(recipes::remove);
    }

    public static Iterator<Map.Entry<ResourceLocation, JsonElement>> sortAndFilterRecipes(Map<ResourceLocation, JsonElement> recipes) {
        Core.debug("[RecipeHelper] Sorting and filtering " + recipes.size() + " recipes");
        return sortRecipes(recipes)
            .filter(RecipeHelper::filterUnloadedRecipeTypes)
            .filter(RecipeHelper::filterUnloadedCharmModules)
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
