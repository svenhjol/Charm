package svenhjol.charm.feature.core.custom_recipes.common;

import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.core.custom_recipes.CustomRecipes;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;

public final class Providers extends ProviderHolder<CustomRecipes> {
    public final List<IConditionalRecipe> conditions = new ArrayList<>();

    public Providers(CustomRecipes feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalRecipeProvider.class,
            provider -> conditions.addAll(provider.getRecipeConditions()));
    }
}
