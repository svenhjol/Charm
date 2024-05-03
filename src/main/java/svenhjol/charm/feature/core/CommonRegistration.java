package svenhjol.charm.feature.core;

import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.foundation.Registration;

public final class CommonRegistration extends Registration<Core> {
    public CommonRegistration(Core feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature::handlePlayerLogin);
    }
}
