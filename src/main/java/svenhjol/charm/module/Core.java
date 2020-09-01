package svenhjol.charm.module;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.client.InventoryButtonClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(alwaysEnabled = true, description = "Core configuration values.")
public class Core extends MesonModule {
    public static final Identifier MSG_SERVER_OPEN_INVENTORY = new Identifier(Charm.MOD_ID, "server_open_inventory");

    @Config(name = "Debug mode", description = "If true, routes additional debug messages into the standard game log.")
    public static boolean debug = false;

    @Config(name = "Inventory button return", description = "If inventory crafting or inventory ender chest modules are enabled, pressing escape or inventory key returns you to the inventory rather than closing the window.")
    public static boolean inventoryButtonReturn = true;

    @Config(
        name = "Charm hack: Enchanting Table",
        description = "If true, changes the code that the vanilla Enchanting Table uses to detect enchanting blocks around it."
    )
    public static boolean hackEnchantingTable = true;

    @Override
    public void initClient() {
        new InventoryButtonClient();

        // listen for network requests to open the player's inventory
        ClientSidePacketRegistry.INSTANCE.register(MSG_SERVER_OPEN_INVENTORY, (context, data) -> {
            context.getTaskQueue().execute(PlayerHelper::openInventory);
        });
    }
}
