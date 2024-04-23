package svenhjol.charm.feature.core;

import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.foundation.Register;

public class CommonRegister extends Register<Core> {
    public CommonRegister(Core feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature::handlePlayerLogin);
    }
}
