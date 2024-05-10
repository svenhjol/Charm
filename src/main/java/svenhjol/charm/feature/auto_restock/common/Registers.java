package svenhjol.charm.feature.auto_restock.common;

import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.feature.auto_restock.AutoRestock;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<AutoRestock> {
    public Registers(AutoRestock feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
    }
}
