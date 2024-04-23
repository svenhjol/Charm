package svenhjol.charm.feature.colored_glints;

import svenhjol.charm.api.event.ClientStartEvent;
import svenhjol.charm.foundation.Register;

public class RegisterClient extends Register<ColoredGlintsClient> {
    public RegisterClient(ColoredGlintsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ClientStartEvent.INSTANCE.handle(feature::handleClientStarted);
    }
}
