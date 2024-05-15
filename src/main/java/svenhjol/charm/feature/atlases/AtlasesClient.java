package svenhjol.charm.feature.atlases;

import svenhjol.charm.feature.atlases.client.Handlers;
import svenhjol.charm.feature.atlases.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class AtlasesClient extends ClientFeature implements CommonResolver<Atlases> {
    public final Registers registers;
    public final Handlers handlers;

    public AtlasesClient(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
        registers = new Registers(this);
    }

    @Override
    public Class<Atlases> typeForCommon() {
        return Atlases.class;
    }
}
