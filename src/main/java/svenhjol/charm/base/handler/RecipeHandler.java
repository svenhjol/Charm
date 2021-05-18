package svenhjol.charm.base.handler;

import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ModHelper;
import svenhjol.charm.base.helper.StringHelper;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class RecipeHandler {
    private static final Map<String, CharmModule> loadedModules = ModuleHandler.getLoadedModules();

    public static void prepareCharmModulesFilter(Map<Identifier, JsonElement> recipes) {
        Map<String, CharmModule> loadedModules = ModuleHandler.getLoadedModules();

        // remove recipes specified by enabled modules
        loadedModules.values().stream().filter(m -> m.enabled && !m.getRecipesToRemove().isEmpty()).forEach(m -> {
            m.getRecipesToRemove().forEach(recipes::remove);
        });
    }

    public static Iterator<Map.Entry<Identifier, JsonElement>> sortAndFilterRecipes(Map<Identifier, JsonElement> recipes) {
        return sortRecipes(recipes)
            .filter(RecipeHandler::filterUnloadedRecipeTypes)
            .filter(RecipeHandler::filterUnloadedCharmModules)
            .iterator();
    }

    /**
     * When crafting recipes are loaded in game, we want modded recipes to take precedence over vanilla.
     * Sort recipes so that all modded recipes are iterated before vanilla recipes.
     */
    private static Stream<Map.Entry<Identifier, JsonElement>> sortRecipes(Map<Identifier, JsonElement> recipes) {
        return Stream.concat(
            recipes.entrySet().stream().filter(r -> !r.getKey().getNamespace().equals("minecraft")),
            recipes.entrySet().stream().filter(r -> r.getKey().getNamespace().equals("minecraft"))
        );
    }

    /**
     * Filter out recipes from Charm modules that have been disabled in config.
     */
    private static boolean filterUnloadedCharmModules(Map.Entry<Identifier, JsonElement> recipe) {
        Identifier id = recipe.getKey();
        if (id.getNamespace().equals("minecraft"))
            return true;

        String path = id.getPath();
        String moduleId = StringHelper.snakeToUpperCamel(path.split("/")[0]);

        // remove recipes for disabled charm modules
        return !loadedModules.containsKey(moduleId) || loadedModules.get(moduleId).enabled;
    }

    /**
     * Filter out recipes where the recipe type refers to a module that isn't loaded.
     */
    private static boolean filterUnloadedRecipeTypes(Map.Entry<Identifier, JsonElement> recipe) {
        Identifier type = new Identifier(JsonHelper.getString(recipe.getValue().getAsJsonObject(), "type"));
        return type.getNamespace().equals("minecraft") || ModHelper.isLoaded(type.getNamespace());
    }
}
