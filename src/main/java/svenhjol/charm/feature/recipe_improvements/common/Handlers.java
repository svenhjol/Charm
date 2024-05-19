package svenhjol.charm.feature.recipe_improvements.common;

import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.FeatureHolder;

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
    public List<IConditionalAdvancement> getAdvancementConditions(Feature owner, IConditionalRecipeProvider provider) {
        var recipePrefix = owner.snakeCaseName() + "/";
        var advancementPrefix = recipePrefix + "recipes/";
        List<IConditionalAdvancement> advancements = new ArrayList<>();

        for (var recipe : provider.getRecipeConditions()) {
            advancements.add(new IConditionalAdvancement() {
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
