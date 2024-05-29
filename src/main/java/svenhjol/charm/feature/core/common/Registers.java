package svenhjol.charm.feature.core.common;

import svenhjol.charm.charmony.event.PlayerLoginEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.core.Core;

public final class Registers extends RegisterHolder<Core> {
    public Registers(Core feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature().handlers::playerLogin);
    }
}
