package svenhjol.charm.feature.recipes;

import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

public class RecipesClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return Recipes.class;
    }
}
