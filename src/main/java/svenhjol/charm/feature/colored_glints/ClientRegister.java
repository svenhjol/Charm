package svenhjol.charm.feature.colored_glints;

import svenhjol.charm.api.event.ClientStartEvent;
import svenhjol.charm.foundation.Register;

public class ClientRegister extends Register<ColoredGlintsClient> {
    public ClientRegister(ColoredGlintsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ClientStartEvent.INSTANCE.handle(feature::handleClientStarted);
    }
}
