package svenhjol.charm.feature.repair_cost_unlimited;

import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Allows anvil repair of items with repair cost 39 or more.")
public final class RepairCostUnlimited extends CommonFeature {
    public RepairCostUnlimited(CommonLoader loader) {
        super(loader);
    }
}
