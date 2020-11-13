package svenhjol.charm.module;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.client.InventoryTidyingClient;
import svenhjol.charm.handler.InventoryTidyingHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

import java.util.List;

import static svenhjol.charm.handler.InventoryTidyingHandler.BE;
import static svenhjol.charm.handler.InventoryTidyingHandler.PLAYER;

@Module(mod = Charm.MOD_ID, client = InventoryTidyingClient.class, description = "Button to automatically tidy inventories.")
public class InventoryTidying extends CharmModule {
    public static final Identifier MSG_SERVER_TIDY_INVENTORY = new Identifier(Charm.MOD_ID, "server_tidy_inventory");

    @Override
    public void init() {
        // listen for network requests to run the server callback
        ServerSidePacketRegistry.INSTANCE.register(MSG_SERVER_TIDY_INVENTORY, (context, data) -> {
            int type = data.readInt();

            context.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
                if (player == null)
                    return;

                InventoryTidying.serverCallback(player, type);
            });
        });

        InventoryTidyingHandler.init();
    }

    public static void serverCallback(ServerPlayerEntity player, int type) {
        ScreenHandler useContainer;

        if (player.isSpectator())
            return;

        if (type == PLAYER && player.playerScreenHandler != null) {
            useContainer = player.playerScreenHandler;
        } else if (type == BE && player.currentScreenHandler != null) {
            useContainer = player.currentScreenHandler;
        } else {
            return;
        }

        List<Slot> slots = useContainer.slots;
        for (Slot slot : slots) {
            Inventory inventory = slot.inventory;

            if (type == PLAYER && slot.inventory == PlayerHelper.getInventory(player)) {
                InventoryTidyingHandler.sort(PlayerHelper.getInventory(player), 9, 36);
                break;
            } else if (type == BE) {
                InventoryTidyingHandler.sort(inventory, 0, inventory.size());
                break;
            }
        }
    }
}
