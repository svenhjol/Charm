package svenhjol.charm.feature.arcane_purpur;

import svenhjol.charm.feature.arcane_purpur.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public class ArcanePurpurClient extends ClientFeature {
    public final ArcanePurpur common;
    public final Registers registers;

    public ArcanePurpurClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(ArcanePurpur.class);
        registers = new Registers(this);
    }
}
