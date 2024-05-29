package svenhjol.charm.feature.recipe_improvements.common;

import svenhjol.charm.api.iface.ConditionalAdvancement;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;

import java.util.ArrayList;
import java.util.List;

public final class Handlers extends FeatureHolder<RecipeImprovements> {
    public Handlers(RecipeImprovements feature) {
        super(feature);
    }

    /**
     * Convenience method to match advancement "recipe" conditions to a set of recipe conditions.
     * This allows advancements that unlock recipes to be enabled/disabled according to a recipe condition.
     */
    public List<ConditionalAdvancement> getAdvancementConditions(Feature owner, ConditionalRecipeProvider provider) {
        var recipePrefix = owner.snakeCaseName() + "/";
        var advancementPrefix = recipePrefix + "recipes/";
        List<ConditionalAdvancement> advancements = new ArrayList<>();

        for (var recipe : provider.getRecipeConditions()) {
            advancements.add(new ConditionalAdvancement() {
                @Override
                public boolean test() {
                    return recipe.test();
                }

                @Override
                public List<String> advancements() {
                    var list = recipe.recipes();
                    return list.stream().map(i -> i.replace(recipePrefix, advancementPrefix)).toList();
                }
            });
        }

        return advancements;
    }
}
