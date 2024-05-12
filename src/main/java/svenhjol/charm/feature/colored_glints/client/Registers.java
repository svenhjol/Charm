package svenhjol.charm.feature.colored_glints.client;

import svenhjol.charm.api.event.ClientStartEvent;
import svenhjol.charm.feature.colored_glints.ColoredGlintsClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ColoredGlintsClient> {
    public Registers(ColoredGlintsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ClientStartEvent.INSTANCE.handle(feature().handlers::handleClientStarted);
    }
}
