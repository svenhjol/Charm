package svenhjol.charm.feature.compass_shows_position.client;

import svenhjol.charm.api.event.HudRenderEvent;
import svenhjol.charm.feature.compass_shows_position.CompassShowsPosition;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CompassShowsPosition>  {
    public Registers(CompassShowsPosition feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        HudRenderEvent.INSTANCE.handle(feature().handlers::hudRender);
    }
}
