package svenhjol.charm.module;

import svenhjol.charm.client.InventoryButtonClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(alwaysEnabled = true, description = "Core configuration values.")
public class Core extends MesonModule {
    @Config(name = "Debug mode", description = "If true, routes additional debug messages into the standard game log.")
    public static boolean debug = false;

    @Config(
        name = "Charm hack: Enchanting Table",
        description = "If true, changes the code that the vanilla Enchanting Table uses to detect enchanting blocks around it."
    )
    public static boolean hackEnchantingTable = true;

    @Override
    public void initClient() {
        new InventoryButtonClient();
    }
}
