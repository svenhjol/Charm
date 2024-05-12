package svenhjol.charm.feature.coral_squids;

import svenhjol.charm.feature.coral_squids.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public final class CoralSquidsClient extends ClientFeature {
    public final Registers registers;
    public final CoralSquids common;

    public CoralSquidsClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(CoralSquids.class);
        registers = new Registers(this);
    }
}
