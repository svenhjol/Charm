package svenhjol.charm.feature.parrots_stay_on_shoulder.common;

import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.parrots_stay_on_shoulder.ParrotsStayOnShoulder;

public final class Registers extends RegisterHolder<ParrotsStayOnShoulder> {
    public Registers(ParrotsStayOnShoulder feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
    }
}
