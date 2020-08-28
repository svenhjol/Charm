package svenhjol.meson.handler;

import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.StringHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeHandler {
    public static void filter(Map<Identifier, JsonElement> recipes) {
        for (String modId : Meson.loadedModules.keySet()) {
            Map<String, MesonModule> modMap = Meson.loadedModules.get(modId);

            // remove recipes specified by enabled modules
            modMap.values().stream().filter(m -> m.enabled && !m.getRecipesToRemove().isEmpty()).forEach(m -> {
                m.getRecipesToRemove().forEach(recipes::remove);
            });

            // fetch all the recipes that match the mod's ID
            List<Identifier> modRecipes = recipes.keySet().stream().filter(r -> r.getNamespace().equals(modId)).collect(Collectors.toList());

            modRecipes.forEach(recipeId -> {
                String path = recipeId.getPath();
                if (!path.contains("/"))
                    return;

                String moduleId = StringHelper.snakeToUpperCamel(path.split("/")[0]);

                // remove recipes for disabled modules
                if (modMap.containsKey(moduleId) && !modMap.get(moduleId).enabled)
                    recipes.remove(recipeId);
            });
        }
    }
}
