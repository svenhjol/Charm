package svenhjol.charm.feature.item_repairing.common;

import svenhjol.charm.charmony.event.AnvilRepairEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.item_repairing.ItemRepairing;

public final class Registers extends RegisterHolder<ItemRepairing> {
    public Registers(ItemRepairing feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        AnvilRepairEvent.INSTANCE.handle(feature().handlers::anvilRepair);
    }
}
