package svenhjol.charm.feature.core;

import svenhjol.charm.feature.core.custom_recipes.CustomRecipesClient;
import svenhjol.charm.feature.core.custom_wood.CustomWoodClient;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.feature.SubFeature;

import java.util.List;

@Feature(priority = 100)
public final class CoreClient extends ClientFeature {
    public CoreClient(ClientLoader loader) {
        super(loader);
    }

    @Override
    public List<? extends SubFeature<? extends svenhjol.charm.foundation.Feature>> subFeatures() {
        return List.of(
            new CustomWoodClient(loader()),
            new CustomRecipesClient(loader())
        );
    }
}
