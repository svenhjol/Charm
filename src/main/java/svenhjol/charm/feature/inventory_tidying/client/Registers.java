package svenhjol.charm.feature.inventory_tidying.client;

import svenhjol.charm.api.event.ScreenRenderEvent;
import svenhjol.charm.api.event.ScreenSetupEvent;
import svenhjol.charm.feature.inventory_tidying.InventoryTidyingClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<InventoryTidyingClient> {
    public Registers(InventoryTidyingClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ScreenSetupEvent.INSTANCE.handle(feature().handlers::screenSetup);
        ScreenRenderEvent.INSTANCE.handle(feature().handlers::screenRender);
    }
}
