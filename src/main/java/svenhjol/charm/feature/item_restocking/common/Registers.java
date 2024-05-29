package svenhjol.charm.feature.item_restocking.common;

import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.feature.item_restocking.ItemRestocking;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ItemRestocking> {
    public Registers(ItemRestocking feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
    }
}
