package svenhjol.charm.feature.recipe_improvements.shapeless_recipes;

import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.feature.recipe_improvements.shapeless_recipes.common.Providers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(description = "Adds convenient shapeless recipe versions of common shaped vanilla recipes.")
public final class ShapelessRecipes extends CommonFeature implements ChildFeature<RecipeImprovements> {
    public final Providers providers;

    @Configurable(name = "Shapeless bread", description = "If true, adds a shapeless recipe for bread.")
    public static boolean bread = true;

    @Configurable(name = "Shapeless paper", description = "If true, adds a shapeless recipe for paper.")
    public static boolean paper = true;

    public ShapelessRecipes(CommonLoader loader) {
        super(loader);

        providers = new Providers(this);
    }

    @Override
    public Class<RecipeImprovements> typeForParent() {
        return RecipeImprovements.class;
    }
}
