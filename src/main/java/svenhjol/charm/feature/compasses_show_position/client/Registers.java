package svenhjol.charm.feature.compasses_show_position.client;

import svenhjol.charm.charmony.event.HudRenderEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.compasses_show_position.CompassesShowPosition;

public final class Registers extends RegisterHolder<CompassesShowPosition>  {
    public Registers(CompassesShowPosition feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        HudRenderEvent.INSTANCE.handle(feature().handlers::hudRender);
    }
}
