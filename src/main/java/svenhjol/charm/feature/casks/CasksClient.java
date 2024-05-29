package svenhjol.charm.feature.casks;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.casks.client.Handlers;
import svenhjol.charm.feature.casks.client.Registers;

@Feature
public final class CasksClient extends ClientFeature implements LinkedFeature<Casks> {
    public final Registers registers;
    public final Handlers handlers;

    public CasksClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<Casks> typeForLinked() {
        return Casks.class;
    }
}
