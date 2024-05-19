package svenhjol.charm.feature.recipe_improvements.bundles_from_leather.common;

import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.recipe_improvements.bundles_from_leather.BundlesFromLeather;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<BundlesFromLeather> implements IConditionalRecipeProvider, IConditionalAdvancementProvider {
    public Providers(BundlesFromLeather feature) {
        super(feature);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        var prefix = feature().snakeCaseName() + "/";

        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().isEnabled();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "bundle");
                }
            }
        );
    }

    @Override
    public List<IConditionalAdvancement> getAdvancementConditions() {
        return feature().parent().handlers.getAdvancementConditions(feature(), this);
    }
}
