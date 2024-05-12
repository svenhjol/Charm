package svenhjol.charm.feature.kilns;

import svenhjol.charm.feature.kilns.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public class KilnsClient extends ClientFeature {
    public final Kilns common;
    public final Registers registers;

    public KilnsClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(Kilns.class);
        registers = new Registers(this);
    }
}
