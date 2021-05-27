package svenhjol.charm.module.core;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.inventory_tidying.InventoryButtonClient;

public class CoreClient extends CharmClientModule {
    public final InventoryButtonClient inventoryButtonClient;

    public CoreClient(CharmModule module) {
        super(module);
        this.inventoryButtonClient = new InventoryButtonClient(module);
    }

    @Override
    public void register() {
        // listen for network requests to open the player's inventory
        ClientPlayNetworking.registerGlobalReceiver(Core.MSG_SERVER_OPEN_INVENTORY, this::handleServerOpenInventory);

        // call the register method of inventoryButtonClient
        this.inventoryButtonClient.register();
    }

    @Override
    public void init() {
        // proxy calls
        this.inventoryButtonClient.init();
    }

    private void handleServerOpenInventory(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        client.execute(ClientHelper::openPlayerInventory);
    }
}
