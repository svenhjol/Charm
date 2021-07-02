package svenhjol.charm.helper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.loader.CommonLoader;
import svenhjol.charm.loader.ModuleLoader;
import svenhjol.charm.module.core.Core;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeHelper {
    private static final List<ResourceLocation> RECIPES_TO_REMOVE = new ArrayList<>();

    public static void removeRecipe(ResourceLocation id) {
        Core.debug("[RecipeHelper] Adding `" + id + "` to list of recipes to remove");
        RECIPES_TO_REMOVE.add(id);
    }

    public static Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> sortAndFilterRecipes(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipesByType) {
        Core.debug("[RecipeHelper] Preparing to sort and filter recipes");
        List<String> modIds = ModuleLoader.getModIds();
        Map<ResourceLocation, CharmModule> charmModules = CommonLoader.getAllModules();
        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> out = new HashMap<>();

        recipesByType.keySet().forEach(type -> {
            Map<ResourceLocation, Recipe<?>> recipes = recipesByType.get(type);
            Core.debug("[RecipeHelper] Recipe type `" + type.toString() + "` contains " + recipes.size() + " recipes");

            Stream<Map.Entry<ResourceLocation, Recipe<?>>> moddedStream = recipes.entrySet().stream().filter(r -> !r.getKey().getNamespace().equals("minecraft"));
            Stream<Map.Entry<ResourceLocation, Recipe<?>>> minecraftStream = recipes.entrySet().stream().filter(r -> r.getKey().getNamespace().equals("minecraft"));

            AtomicInteger countActual = new AtomicInteger();
            AtomicInteger countFiltered = new AtomicInteger();

            moddedStream = moddedStream.filter(r -> {
                countActual.getAndIncrement();

                ResourceLocation res = r.getKey();
                String namespace = res.getNamespace();
                String path = res.getPath();

                // if the recipe is not a charm-based mod, let it pass
                if (!modIds.contains(namespace)) return true;

                String moduleId = StringHelper.upperCamelToSnake(path.split("/")[0]);
                ResourceLocation check = new ResourceLocation(namespace, moduleId);

                // remove recipes for disabled charm modules and recipes
                boolean enabled = charmModules.containsKey(check)
                    && charmModules.get(check).isEnabled()
                    && !RECIPES_TO_REMOVE.contains(res);

                if (!enabled) {
                    Core.debug("[RecipeHelper] > Filtering out recipe `" + res + "`");
                    countFiltered.getAndIncrement();
                }

                return enabled;
            });

            Map<ResourceLocation, Recipe<?>> merged = new TreeMap<>();
            Map<ResourceLocation, Recipe<?>> moddedRecipes = moddedStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Map<ResourceLocation, Recipe<?>> minecraftRecipes = minecraftStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            // rebuild the recipes for this type
            merged.putAll(moddedRecipes);
            merged.putAll(minecraftRecipes);

            out.put(type, merged);
            Core.debug("[RecipeHelper] Recipe type `" + type + "` reassembled with " + merged.size() + " recipes");
        });

        return out;
    }
}
