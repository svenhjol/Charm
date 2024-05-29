package svenhjol.charm.feature.item_repairing.common;

import svenhjol.charm.api.event.AnvilRepairEvent;
import svenhjol.charm.feature.item_repairing.ItemRepairing;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ItemRepairing> {
    public Registers(ItemRepairing feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        AnvilRepairEvent.INSTANCE.handle(feature().handlers::anvilRepair);
    }
}
