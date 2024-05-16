package svenhjol.charm.feature.core.recipes.common;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.feature.core.recipes.Recipes;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public final class Handlers extends FeatureHolder<Recipes> {
    private final List<String> fuzzyRemove = new ArrayList<>();
    private final List<String> exactRemove = new ArrayList<>();

    /**
     * Holds a reference to the global RecipeManager.
     */
    public RecipeManager managerHolder;

    public Handlers(Recipes feature) {
        super(feature);
    }

    public void packReload(String reason) {
        feature().log().debug("Reloading Charm custom recipe filtering: " + reason);

        exactRemove.clear();
        fuzzyRemove.clear();

        for (var condition : feature().providers.conditions) {
            if (condition.test()) continue;
            condition.recipes().forEach(remove -> {
                if (remove.contains("*") || !remove.contains(":")) {
                    fuzzyRemove.add(remove);
                } else {
                    exactRemove.add(remove);
                }
            });
        }
    }

    /**
     * Called by sorting recipe manager.
     *
     * @see RecipeSorter
     * original: Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>>
     * new: Multimap<RecipeType<?>, RecipeHolder<?>>
     */
    public Multimap<RecipeType<?>, RecipeHolder<?>> sortAndFilter(Multimap<RecipeType<?>, RecipeHolder<?>> byType) {
        feature().log().debug("Preparing to sort and filter recipes.");

        ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder = ImmutableMultimap.builder();

        for (var type : byType.keySet()) {
            var recipes = byType.get(type);
            feature().log().debug("Recipe type " + type.toString() + " contains " + recipes.size() + " recipes.");

            var charmonyRecipes = recipes.stream().filter(r -> Resolve.hasLoader(Side.COMMON, r.id().getNamespace()));
            var otherRecipes = recipes.stream().filter(r -> !Resolve.hasLoader(Side.COMMON, r.id().getNamespace()));
            var holders = new LinkedList<RecipeHolder<?>>();

            // Sort and filter the charmony recipes.
            charmonyRecipes
                .filter(r -> !shouldRemove(r.id())).sorted(Comparator.comparing(r -> r.id().getPath()))
                .forEach(holders::add);

            // Add other recipes after charm's so that they're parsed afterwards.
            otherRecipes.forEach(holders::add);

            holders.forEach(holder -> builder.put(type, holder));
            feature().log().debug("Recipe type " + type + " reassembled with " + holders.size() + " recipes");
        }

        return builder.build();
    }

    public boolean shouldRemove(ResourceLocation id) {
        return ResourceLocationHelper.isDisabledCharmonyFeature(id)
            || ResourceLocationHelper.match(id, exactRemove, fuzzyRemove);
    }
}
