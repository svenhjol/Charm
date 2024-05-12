package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charm.feature.totem_of_preserving.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public final class TotemOfPreservingClient extends ClientFeature {
    public final Registers registers;
    public final TotemOfPreserving common;

    public TotemOfPreservingClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(TotemOfPreserving.class);
        registers = new Registers(this);
    }
}
