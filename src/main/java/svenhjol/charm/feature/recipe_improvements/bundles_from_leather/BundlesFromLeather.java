package svenhjol.charm.feature.recipe_improvements.bundles_from_leather;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.feature.recipe_improvements.bundles_from_leather.common.Providers;

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
