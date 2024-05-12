package svenhjol.charm.feature.unlimited_repair_cost;

import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Allows anvil repair of items with repair cost 39 or more.")
public final class UnlimitedRepairCost extends CommonFeature {
    public UnlimitedRepairCost(CommonLoader loader) {
        super(loader);
    }
}
