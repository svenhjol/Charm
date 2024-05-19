package svenhjol.charm.feature.recipe_improvements.shapeless_recipes.common;

import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.recipe_improvements.shapeless_recipes.ShapelessRecipes;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<ShapelessRecipes> implements IConditionalRecipeProvider, IConditionalAdvancementProvider {
    public Providers(ShapelessRecipes feature) {
        super(feature);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        var prefix = feature().snakeCaseName() + "/";

        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ShapelessRecipes.bread;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "bread");
                }
            },

            new IConditionalRecipe() {
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
    public List<IConditionalAdvancement> getAdvancementConditions() {
        return feature().parent().handlers.getAdvancementConditions(feature(), this);
    }
}
