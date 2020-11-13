package svenhjol.charm.client;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.module.Core;

public class CoreClient extends CharmClientModule {
    public final InventoryButtonClient inventoryButtonClient;

    public CoreClient(CharmModule module) {
        super(module);
        this.inventoryButtonClient = new InventoryButtonClient(module);
    }

    @Override
    public void register() {
        // listen for network requests to open the player's inventory
        ClientSidePacketRegistry.INSTANCE.register(Core.MSG_SERVER_OPEN_INVENTORY, (context, data) -> {
            context.getTaskQueue().execute(PlayerHelper::openInventory);
        });
    }
}
