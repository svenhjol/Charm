package svenhjol.charm.feature.item_hover_sorting.client;

import svenhjol.charm.charmony.event.MouseScrollEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSortingClient;

public final class Registers extends RegisterHolder<ItemHoverSortingClient> {
    public Registers(ItemHoverSortingClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        MouseScrollEvent.ON_SCREEN.handle(feature().handlers::mouseScroll);
    }
}
