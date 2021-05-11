package svenhjol.charm.module;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.InventoryTidyingClient;
import svenhjol.charm.handler.InventoryTidyingHandler;
import svenhjol.charm.init.CharmAdvancements;

import java.util.List;

import static svenhjol.charm.handler.InventoryTidyingHandler.BE;
import static svenhjol.charm.handler.InventoryTidyingHandler.PLAYER;

@Module(mod = Charm.MOD_ID, client = InventoryTidyingClient.class, description = "Button to automatically tidy inventories.")
public class InventoryTidying extends CharmModule {
    public static final Identifier MSG_SERVER_TIDY_INVENTORY = new Identifier(Charm.MOD_ID, "server_tidy_inventory");
    public static final Identifier TRIGGER_TIDIED_INVENTORY = new Identifier(Charm.MOD_ID, "tidied_inventory");

    @Override
    public void init() {
        // listen for network requests to run the server callback
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_TIDY_INVENTORY, this::handleServerTidyInventory);

        InventoryTidyingHandler.init();
    }

    private void handleServerTidyInventory(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        int type = data.readInt();

        server.execute(() -> {
            if (player == null)
                return;

            InventoryTidying.serverCallback(player, type);
        });
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

        // do advancement for tidying inventory
        InventoryTidying.triggerTidiedInventory(player);
    }

    public static void triggerTidiedInventory(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_TIDIED_INVENTORY);
    }
}
