package svenhjol.charm.feature.recipe_improvements.shapeless_recipes.common;

import svenhjol.charm.api.iface.ConditionalAdvancement;
import svenhjol.charm.api.iface.ConditionalAdvancementProvider;
import svenhjol.charm.api.iface.ConditionalRecipe;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.recipe_improvements.shapeless_recipes.ShapelessRecipes;

import java.util.List;

public final class Providers extends ProviderHolder<ShapelessRecipes> implements ConditionalRecipeProvider, ConditionalAdvancementProvider {
    public Providers(ShapelessRecipes feature) {
        super(feature);
    }

    @Override
    public List<ConditionalRecipe> getRecipeConditions() {
        var prefix = feature().snakeCaseName() + "/";

        return List.of(
            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return ShapelessRecipes.bread;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "bread");
                }
            },

            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return ShapelessRecipes.paper;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "paper");
                }
            }
        );
    }

    @Override
    public List<ConditionalAdvancement> getAdvancementConditions() {
        return feature().parent().handlers.getAdvancementConditions(feature(), this);
    }
}
