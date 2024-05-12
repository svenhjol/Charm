package svenhjol.charm.feature.show_repair_cost;

import svenhjol.charm.feature.show_repair_cost.client.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "An item's repair cost is shown in their tooltip when looking at the anvil screen.")
public final class ShowRepairCost extends ClientFeature {
    public final Handlers handlers;

    public ShowRepairCost(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
