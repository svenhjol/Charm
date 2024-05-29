package svenhjol.charm.feature.recipe_improvements.bundles_from_leather.common;

import svenhjol.charm.api.iface.ConditionalAdvancement;
import svenhjol.charm.api.iface.ConditionalAdvancementProvider;
import svenhjol.charm.api.iface.ConditionalRecipe;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.recipe_improvements.bundles_from_leather.BundlesFromLeather;

import java.util.List;

public final class Providers extends ProviderHolder<BundlesFromLeather> implements ConditionalRecipeProvider, ConditionalAdvancementProvider {
    public Providers(BundlesFromLeather feature) {
        super(feature);
    }

    @Override
    public List<ConditionalRecipe> getRecipeConditions() {
        var prefix = feature().snakeCaseName() + "/";

        return List.of(
            new ConditionalRecipe() {
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
    public List<ConditionalAdvancement> getAdvancementConditions() {
        return feature().parent().handlers.getAdvancementConditions(feature(), this);
    }
}
