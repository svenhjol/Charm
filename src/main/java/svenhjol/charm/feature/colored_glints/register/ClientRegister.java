package svenhjol.charm.feature.colored_glints.register;

import svenhjol.charm.api.event.ClientStartEvent;
import svenhjol.charm.feature.colored_glints.ColoredGlintsClient;
import svenhjol.charm.foundation.Register;

public class ClientRegister extends Register<ColoredGlintsClient> {
    public ClientRegister(ColoredGlintsClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        if (feature.isEnabled()) {
            ClientStartEvent.INSTANCE.handle(feature::handleClientStarted);
        }
    }
}
