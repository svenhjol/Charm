package svenhjol.charm.feature.copper_pistons;

import svenhjol.charm.feature.copper_pistons.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public class CopperPistonsClient extends ClientFeature {
    public final CopperPistons common;
    public final Registers registers;

    public CopperPistonsClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(CopperPistons.class);
        registers = new Registers(this);
    }
}
