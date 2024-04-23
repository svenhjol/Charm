package svenhjol.charm.feature.recipes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.helper.ApiHelper;
import svenhjol.charm.foundation.helper.ResourceLocationHelper;

import java.util.*;

public class Recipes extends CommonFeature {
    /**
     * Holds a reference to the global RecipeManager.
     * @see svenhjol.charm.mixin.recipes.ReloadableServerResourcesMixin
     */
    public static RecipeManager managerHolder;
    static final List<String> FUZZY_REMOVE = new ArrayList<>();
    static final List<String> EXACT_REMOVE = new ArrayList<>();
    static final List<IConditionalRecipe> CONDITIONS = new ArrayList<>();

    @Override
    public String description() {
        return """
            Filter recipes when Charmony-mod features or settings are disabled.
            Disabling this feature will cause unexpected behavior and potentially unachievable recipes.""";
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalRecipeProvider.class,
            provider -> CONDITIONS.addAll(provider.getRecipeConditions()));
    }

    public static void handlePackReload(String reason) {
        var log = Globals.common(Charm.ID).log();
        log.debug(Recipes.class, "Reloading Charm custom recipe filtering: " + reason);

        EXACT_REMOVE.clear();
        FUZZY_REMOVE.clear();

        for (var condition : CONDITIONS) {
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
     * @see svenhjol.charmony.recipe.SortingRecipeManager
     * original: Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>>
     * new: Multimap<RecipeType<?>, RecipeHolder<?>>
     */
    public static Multimap<RecipeType<?>, RecipeHolder<?>> sortAndFilter(Multimap<RecipeType<?>, RecipeHolder<?>> byType) {
        var log = Globals.common(Charm.ID).log();
        log.debug(Recipes.class, "Preparing to sort and filter recipes.");

        ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder = ImmutableMultimap.builder();

        for (var type : byType.keySet()) {
            var recipes = byType.get(type);
            log.debug(Recipes.class, "Recipe type " + type.toString() + " contains " + recipes.size() + " recipes.");

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
            log.debug(Recipes.class, "Recipe type " + type + " reassembled with " + holders.size() + " recipes");
        }

        return builder.build();
    }

    public static boolean shouldRemove(ResourceLocation id) {
        return ResourceLocationHelper.isDisabledCharmonyFeature(id)
            || ResourceLocationHelper.match(id, EXACT_REMOVE, FUZZY_REMOVE);
    }
}
