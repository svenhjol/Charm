package svenhjol.charm.feature.core.common;

import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<Core> {
    public Registers(Core feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature().handlers::playerLogin);
    }
}
