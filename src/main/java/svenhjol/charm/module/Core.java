package svenhjol.charm.module;

import svenhjol.charm.client.InventoryButtonClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(alwaysEnabled = true, description = "Core configuration values.")
public class Core extends MesonModule {
    @Config(name = "Debug mode", description = "If true, routes additional debug messages into the standard game log.")
    public static boolean debug = false;

    @Override
    public void initClient() {
        new InventoryButtonClient();
    }
}
