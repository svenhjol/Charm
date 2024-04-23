package svenhjol.charm.feature.core;

import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.foundation.Register;

public class RegisterCommon extends Register<Core> {
    public RegisterCommon(Core feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature::handlePlayerLogin);
    }
}
