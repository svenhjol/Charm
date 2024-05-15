package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charm.feature.totem_of_preserving.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class TotemOfPreservingClient extends ClientFeature implements CommonResolver<TotemOfPreserving> {
    public final Registers registers;

    public TotemOfPreservingClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<TotemOfPreserving> typeForCommon() {
        return TotemOfPreserving.class;
    }
}
