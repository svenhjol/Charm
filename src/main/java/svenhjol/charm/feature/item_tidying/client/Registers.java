package svenhjol.charm.feature.item_tidying.client;

import svenhjol.charm.charmony.event.ScreenRenderEvent;
import svenhjol.charm.charmony.event.ScreenSetupEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.item_tidying.ItemTidyingClient;

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
