package svenhjol.charm.feature.repair_cost_visible;

import svenhjol.charm.feature.repair_cost_visible.client.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "An item's repair cost is shown in its tooltip when viewing on the anvil.")
public final class RepairCostVisible extends ClientFeature {
    public final Handlers handlers;

    public RepairCostVisible(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
