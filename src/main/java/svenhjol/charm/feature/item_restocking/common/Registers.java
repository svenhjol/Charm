package svenhjol.charm.feature.item_restocking.common;

import svenhjol.charm.charmony.event.PlayerTickEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.item_restocking.ItemRestocking;

public final class Registers extends RegisterHolder<ItemRestocking> {
    public Registers(ItemRestocking feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
    }
}
