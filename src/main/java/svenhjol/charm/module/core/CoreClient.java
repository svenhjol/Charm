package svenhjol.charm.module.core;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.inventory_tidying.InventoryButtonClient;

@ClientModule(module = Core.class)
public class CoreClient extends CharmModule {
    public final InventoryButtonClient inventoryButtonClient;

    public CoreClient() {
        this.inventoryButtonClient = new InventoryButtonClient();
    }

    @Override
    public void register() {
        // listen for network requests to open the player's inventory
        ClientPlayNetworking.registerGlobalReceiver(Core.MSG_SERVER_OPEN_INVENTORY, this::handleServerOpenInventory);

        // call the register method of inventoryButtonClient
        this.inventoryButtonClient.register();
    }

    @Override
    public void runWhenEnabled() {
        // proxy calls
        this.inventoryButtonClient.runWhenEnabled();
    }

    private void handleServerOpenInventory(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        client.execute(ClientHelper::openPlayerInventory);
    }
}
