package svenhjol.charm.feature.item_tidying.client;

import svenhjol.charm.api.event.ScreenRenderEvent;
import svenhjol.charm.api.event.ScreenSetupEvent;
import svenhjol.charm.feature.item_tidying.ItemTidyingClient;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ItemTidyingClient> {
    public Registers(ItemTidyingClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ScreenSetupEvent.INSTANCE.handle(feature().handlers::screenSetup);
        ScreenRenderEvent.INSTANCE.handle(feature().handlers::screenRender);
    }
}
