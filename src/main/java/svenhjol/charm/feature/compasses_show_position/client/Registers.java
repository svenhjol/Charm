package svenhjol.charm.feature.compasses_show_position.client;

import svenhjol.charm.api.event.HudRenderEvent;
import svenhjol.charm.feature.compasses_show_position.CompassesShowPosition;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CompassesShowPosition>  {
    public Registers(CompassesShowPosition feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        HudRenderEvent.INSTANCE.handle(feature().handlers::hudRender);
    }
}
