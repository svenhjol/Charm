package svenhjol.charm.feature.core;

import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.foundation.feature.Register;

public final class CommonRegistration extends Register<Core> {
    public CommonRegistration(Core feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature::handlePlayerLogin);
    }
}
