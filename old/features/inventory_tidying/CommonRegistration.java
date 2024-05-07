package svenhjol.charm.feature.inventory_tidying;

import svenhjol.charm.foundation.feature.Register;

public final class CommonRegistration extends Register<InventoryTidying> {
    public CommonRegistration(InventoryTidying feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        InventoryTidyingHandler.init();
    }
}
