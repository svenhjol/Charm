package svenhjol.charm.module;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.client.InventorySortingClient;
import svenhjol.charm.handler.InventorySortingHandler;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.List;

import static svenhjol.charm.handler.InventorySortingHandler.PLAYER;
import static svenhjol.charm.handler.InventorySortingHandler.TILE;

@Module(description = "Button to automatically tidy inventories.")
public class InventorySorting extends MesonModule {
    public static InventorySortingClient client;
    public static final Identifier MSG_SERVER_SORT_INVENTORY = new Identifier(Charm.MOD_ID, "server_sort_inventory");

    @Override
    public void init() {

        // listen for network requests to run the server callback
        ServerSidePacketRegistry.INSTANCE.register(MSG_SERVER_SORT_INVENTORY, (context, data) -> {
            int type = data.readInt();

            context.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
                if (player == null)
                    return;

                InventorySorting.serverCallback(player, type);
            });
        });

        InventorySortingHandler.init();
    }

    @Override
    public void initClient() {
        client = new InventorySortingClient(this);
    }

    public static void serverCallback(ServerPlayerEntity player, int type) {
        ScreenHandler useContainer;

        if (player.isSpectator())
            return;

        if (type == PLAYER && player.playerScreenHandler != null) {
            useContainer = player.playerScreenHandler;
        } else if (type == TILE && player.currentScreenHandler != null) {
            useContainer = player.currentScreenHandler;
        } else {
            return;
        }

        List<Slot> slots = useContainer.slots;
        for (Slot slot : slots) {
            Inventory inventory = slot.inventory;

            if (type == PLAYER && slot.inventory == player.inventory) {
                InventorySortingHandler.sort(player.inventory, 9, 36);
                break;
            } else if (type == TILE) {
                InventorySortingHandler.sort(inventory, 0, inventory.size());
                break;
            }
        }
    }
}
