package svenhjol.charm.feature.parrots_stay_on_shoulder.common;

import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.feature.parrots_stay_on_shoulder.ParrotsStayOnShoulder;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ParrotsStayOnShoulder> {
    public Registers(ParrotsStayOnShoulder feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
    }
}
