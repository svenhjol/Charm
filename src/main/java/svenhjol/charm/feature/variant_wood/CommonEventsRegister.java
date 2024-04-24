package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.foundation.Register;

public class CommonEventsRegister extends Register<VariantWood> {
    public CommonEventsRegister(VariantWood feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        CharmApi.registerProvider(new DataProviders());
    }

    @Override
    public void onEnabled() {
        EntityUseEvent.INSTANCE.handle(AnimalInteraction::handle);
    }
}
