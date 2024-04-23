package svenhjol.charm.feature.recipes;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.helper.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public final class CommonCallbacks {
    static final List<String> FUZZY_REMOVE = new ArrayList<>();
    static final List<String> EXACT_REMOVE = new ArrayList<>();

    public static void handlePackReload(String reason) {
        Recipes.LOGGER.debug("Reloading Charm custom recipe filtering: " + reason);

        EXACT_REMOVE.clear();
        FUZZY_REMOVE.clear();

        for (var condition : Recipes.CONDITIONS) {
            if (condition.test()) continue;
            condition.recipes().forEach(remove -> {
                if (remove.contains("*") || !remove.contains(":")) {
                    FUZZY_REMOVE.add(remove);
                } else {
                    EXACT_REMOVE.add(remove);
                }
            });
        }
    }

    /**
     * Called by sorting recipe manager.
     *
     * @see svenhjol.charm.feature.recipes.SortingRecipeManager
     * original: Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>>
     * new: Multimap<RecipeType<?>, RecipeHolder<?>>
     */
    public static Multimap<RecipeType<?>, RecipeHolder<?>> sortAndFilter(Multimap<RecipeType<?>, RecipeHolder<?>> byType) {
        Recipes.LOGGER.debug("Preparing to sort and filter recipes.");

        ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder = ImmutableMultimap.builder();

        for (var type : byType.keySet()) {
            var recipes = byType.get(type);
            Recipes.LOGGER.debug("Recipe type " + type.toString() + " contains " + recipes.size() + " recipes.");

            var charmonyRecipes = recipes.stream().filter(r -> Globals.has(Side.COMMON, r.id().getNamespace()));
            var otherRecipes = recipes.stream().filter(r -> !Globals.has(Side.COMMON, r.id().getNamespace()));
            var holders = new LinkedList<RecipeHolder<?>>();

            // Sort and filter the charmony recipes.
            charmonyRecipes
                .filter(r -> !shouldRemove(r.id())).sorted(Comparator.comparing(r -> r.id().getPath()))
                .forEach(holders::add);

            // Add other recipes after charm's so that they're parsed afterwards.
            otherRecipes.forEach(holders::add);

            holders.forEach(holder -> builder.put(type, holder));
            Recipes.LOGGER.debug("Recipe type " + type + " reassembled with " + holders.size() + " recipes");
        }

        return builder.build();
    }

    public static boolean shouldRemove(ResourceLocation id) {
        return ResourceLocationHelper.isDisabledCharmonyFeature(id)
            || ResourceLocationHelper.match(id, EXACT_REMOVE, FUZZY_REMOVE);
    }
}
