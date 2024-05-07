package svenhjol.charm.feature.totems_work_from_inventory;

import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.iface.ITotemInventoryCheckProvider;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.helper.ApiHelper;

public final class CommonRegistration extends Register<TotemsWorkFromInventory> {
    public CommonRegistration(TotemsWorkFromInventory feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        CharmApi.registerProvider(new TotemInventoryCheckProvider());
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(ITotemInventoryCheckProvider.class,
            provider -> TotemsWorkFromInventory.inventoryCheckProviders.add(provider));
    }
}
