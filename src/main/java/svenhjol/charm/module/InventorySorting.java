package svenhjol.charm.module;

import svenhjol.charm.client.InventorySortingClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Button to automatically tidy inventories.")
public class InventorySorting extends MesonModule {
    public static InventorySortingClient client;

    @Override
    public void initClient() {
        client = new InventorySortingClient(this);
    }
}
