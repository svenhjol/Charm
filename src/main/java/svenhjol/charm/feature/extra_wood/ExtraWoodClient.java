package svenhjol.charm.feature.extra_wood;

import svenhjol.charm.feature.extra_wood.azalea_wood.AzaleaWoodClient;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.List;

@Feature
public final class ExtraWoodClient extends ClientFeature {
    public ExtraWoodClient(ClientLoader loader) {
        super(loader);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.foundation.Feature>> children() {
        return List.of(
            new AzaleaWoodClient(loader())
        );
    }
}
