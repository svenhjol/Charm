package svenhjol.charm.feature.atlases;

import svenhjol.charm.feature.atlases.client.Handlers;
import svenhjol.charm.feature.atlases.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public final class AtlasesClient extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Atlases common;

    public AtlasesClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(Atlases.class);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }
}
