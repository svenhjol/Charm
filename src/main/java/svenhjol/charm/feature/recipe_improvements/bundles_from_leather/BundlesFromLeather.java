package svenhjol.charm.feature.recipe_improvements.bundles_from_leather;

import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.feature.recipe_improvements.bundles_from_leather.common.Providers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(description = "Adds a leather-based recipe for the vanilla bundle.")
public final class BundlesFromLeather extends CommonFeature implements ChildFeature<RecipeImprovements> {
    public final Providers providers;

    public BundlesFromLeather(CommonLoader loader) {
        super(loader);

        providers = new Providers(this);
    }

    @Override
    public Class<RecipeImprovements> typeForParent() {
        return RecipeImprovements.class;
    }
}
