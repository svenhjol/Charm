package svenhjol.charm.feature.casks;

import svenhjol.charm.feature.casks.client.Handlers;
import svenhjol.charm.feature.casks.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class CasksClient extends ClientFeature implements CommonResolver<Casks> {
    public final Registers registers;
    public final Handlers handlers;

    public CasksClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<Casks> typeForCommon() {
        return Casks.class;
    }
}
