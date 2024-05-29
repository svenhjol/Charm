package svenhjol.charm.feature.core;

import svenhjol.charm.feature.core.custom_recipes.CustomRecipesClient;
import svenhjol.charm.feature.core.custom_wood.CustomWoodClient;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

import java.util.List;

@Feature(priority = 100)
public final class CoreClient extends ClientFeature {
    public CoreClient(ClientLoader loader) {
        super(loader);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.charmony.Feature>> children() {
        return List.of(
            new CustomRecipesClient(loader()),
            new CustomWoodClient(loader())
        );
    }
}
