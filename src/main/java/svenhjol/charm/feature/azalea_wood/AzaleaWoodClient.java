package svenhjol.charm.feature.azalea_wood;

import svenhjol.charm.feature.azalea_wood.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public final class AzaleaWoodClient extends ClientFeature {
    public final AzaleaWood common;
    public final Registers registers;

    public AzaleaWoodClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(AzaleaWood.class);
        registers = new Registers(this);
    }
}
