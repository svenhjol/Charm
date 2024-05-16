package svenhjol.charm.feature.core.custom_recipes;

import svenhjol.charm.feature.core.CoreClient;
import svenhjol.charm.feature.core.custom_recipes.client.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature(priority = 90)
public final class CustomRecipesClient extends ClientFeature implements SubFeature<CoreClient> {
    public final Handlers handlers;

    public CustomRecipesClient(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public Class<CoreClient> typeForParent() {
        return CoreClient.class;
    }
}
