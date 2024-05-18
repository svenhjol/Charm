package svenhjol.charm.feature.compass_overlay.client;

import svenhjol.charm.api.event.HudRenderEvent;
import svenhjol.charm.feature.compass_overlay.CompassOverlay;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CompassOverlay>  {
    public Registers(CompassOverlay feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        HudRenderEvent.INSTANCE.handle(feature().handlers::hudRender);
    }
}
