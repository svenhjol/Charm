package svenhjol.charm.feature.colored_glints;

import svenhjol.charm.api.event.ClientStartEvent;
import svenhjol.charm.foundation.Registration;

public class ClientRegistration extends Registration<ColoredGlintsClient> {
    public ClientRegistration(ColoredGlintsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ClientStartEvent.INSTANCE.handle(feature::handleClientStarted);
    }
}
