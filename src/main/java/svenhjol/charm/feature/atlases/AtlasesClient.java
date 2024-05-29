package svenhjol.charm.feature.atlases;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.atlases.client.Handlers;
import svenhjol.charm.feature.atlases.client.Registers;

@Feature
public final class AtlasesClient extends ClientFeature implements LinkedFeature<Atlases> {
    public final Registers registers;
    public final Handlers handlers;

    public AtlasesClient(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
        registers = new Registers(this);
    }

    @Override
    public Class<Atlases> typeForLinked() {
        return Atlases.class;
    }
}
