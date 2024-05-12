package svenhjol.charm.feature.custom_wood;

import svenhjol.charm.feature.custom_wood.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(priority = -10)
public final class CustomWoodClient extends ClientFeature {
    public final CustomWood common;
    public final Registers registers;

    public CustomWoodClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(CustomWood.class);
        registers = new Registers(this);
    }
}
