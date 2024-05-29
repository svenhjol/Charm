package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.totem_of_preserving.client.Registers;

@Feature
public final class TotemOfPreservingClient extends ClientFeature implements LinkedFeature<TotemOfPreserving> {
    public final Registers registers;

    public TotemOfPreservingClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<TotemOfPreserving> typeForLinked() {
        return TotemOfPreserving.class;
    }
}
