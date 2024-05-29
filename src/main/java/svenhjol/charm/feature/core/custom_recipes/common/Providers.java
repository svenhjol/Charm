package svenhjol.charm.feature.core.custom_recipes.common;

import svenhjol.charm.api.iface.ConditionalRecipe;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.feature.core.custom_recipes.CustomRecipes;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.charmony.Api;

import java.util.ArrayList;
import java.util.List;

public final class Providers extends ProviderHolder<CustomRecipes> {
    public final List<ConditionalRecipe> conditions = new ArrayList<>();

    public Providers(CustomRecipes feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        Api.consume(ConditionalRecipeProvider.class,
            provider -> conditions.addAll(provider.getRecipeConditions()));
    }
}
