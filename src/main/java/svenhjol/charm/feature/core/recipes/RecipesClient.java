package svenhjol.charm.feature.core.recipes;

import svenhjol.charm.feature.core.CoreClient;
import svenhjol.charm.feature.core.recipes.client.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature(priority = 90)
public final class RecipesClient extends ClientFeature implements SubFeature<CoreClient> {
    public final Handlers handlers;

    public RecipesClient(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public Class<CoreClient> typeForParent() {
        return CoreClient.class;
    }
}
