package svenhjol.charm.feature.wood;

import svenhjol.charm.feature.wood.azalea_wood.AzaleaWoodClient;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.List;

@Feature
public final class WoodClient extends ClientFeature {
    public WoodClient(ClientLoader loader) {
        super(loader);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.foundation.Feature>> children() {
        return List.of(
            new AzaleaWoodClient(loader())
        );
    }
}
