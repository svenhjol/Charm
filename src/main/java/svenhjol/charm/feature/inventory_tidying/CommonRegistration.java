package svenhjol.charm.feature.inventory_tidying;

import svenhjol.charm.foundation.Registration;

public final class CommonRegistration extends Registration<InventoryTidying> {
    public CommonRegistration(InventoryTidying feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        InventoryTidyingHandler.init();
    }
}
