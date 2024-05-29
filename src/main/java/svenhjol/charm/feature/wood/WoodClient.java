package svenhjol.charm.feature.wood;

import svenhjol.charm.feature.wood.azalea_wood.AzaleaWoodClient;
import svenhjol.charm.feature.wood.ebony_wood.EbonyWoodClient;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

import java.util.List;

@Feature
public final class WoodClient extends ClientFeature {
    public WoodClient(ClientLoader loader) {
        super(loader);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.charmony.Feature>> children() {
        return List.of(
            new AzaleaWoodClient(loader()),
            new EbonyWoodClient(loader())
        );
    }
}
