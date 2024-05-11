package svenhjol.charm.feature.hover_sorting.client;

import svenhjol.charm.api.event.MouseScrollEvent;
import svenhjol.charm.feature.hover_sorting.HoverSortingClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<HoverSortingClient> {
    public Registers(HoverSortingClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        MouseScrollEvent.ON_SCREEN.handle(feature().handlers::mouseScroll);
    }
}
