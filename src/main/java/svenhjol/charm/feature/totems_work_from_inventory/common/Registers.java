package svenhjol.charm.feature.totems_work_from_inventory.common;

import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.iface.ITotemInventoryCheckProvider;
import svenhjol.charm.feature.totems_work_from_inventory.TotemsWorkFromInventory;
import svenhjol.charm.foundation.feature.RegisterHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<TotemsWorkFromInventory> {
    public final List<ITotemInventoryCheckProvider> inventoryCheckProviders = new ArrayList<>();

    public Registers(TotemsWorkFromInventory feature) {
        super(feature);

        CharmApi.registerProvider(new DataProviders(feature));
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(ITotemInventoryCheckProvider.class, inventoryCheckProviders::add);
    }
}
