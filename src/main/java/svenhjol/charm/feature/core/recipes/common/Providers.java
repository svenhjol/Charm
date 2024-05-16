package svenhjol.charm.feature.core.recipes.common;

import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.core.recipes.Recipes;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;

public final class Providers extends ProviderHolder<Recipes> {
    public final List<IConditionalRecipe> conditions = new ArrayList<>();

    public Providers(Recipes feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalRecipeProvider.class,
            provider -> conditions.addAll(provider.getRecipeConditions()));
    }
}
