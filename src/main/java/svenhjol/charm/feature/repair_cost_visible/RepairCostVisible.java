package svenhjol.charm.feature.repair_cost_visible;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.feature.repair_cost_visible.client.Handlers;

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
