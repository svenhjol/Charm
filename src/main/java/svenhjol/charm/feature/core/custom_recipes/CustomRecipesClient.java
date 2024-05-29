package svenhjol.charm.feature.core.custom_recipes;

import svenhjol.charm.feature.core.CoreClient;
import svenhjol.charm.feature.core.custom_recipes.client.Handlers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature(priority = 90)
public final class CustomRecipesClient extends ClientFeature implements ChildFeature<CoreClient> {
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
