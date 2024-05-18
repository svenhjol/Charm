package svenhjol.charm.feature.glint_coloring.client;

import svenhjol.charm.api.event.ClientStartEvent;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<GlintColoringClient> {
    public Registers(GlintColoringClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ClientStartEvent.INSTANCE.handle(feature().handlers::handleClientStarted);
    }
}
