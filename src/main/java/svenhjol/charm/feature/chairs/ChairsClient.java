package svenhjol.charm.feature.chairs;

import svenhjol.charm.feature.chairs.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public class ChairsClient extends ClientFeature {
    public final Registers registers;
    public final Chairs common;

    public ChairsClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(Chairs.class);
        registers = new Registers(this);
    }
}
