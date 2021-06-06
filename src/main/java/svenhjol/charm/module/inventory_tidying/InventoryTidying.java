package svenhjol.charm.module.inventory_tidying;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.module.CharmModule;

import java.util.List;

import static svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler.BE;
import static svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler.PLAYER;

@Module(mod = Charm.MOD_ID, client = InventoryTidyingClient.class, description = "Button to automatically tidy inventories.",
    requiresMixins = {"SetupGuiCallback", "RenderGuiCallback"})
public class InventoryTidying extends CharmModule {
    public static final ResourceLocation MSG_SERVER_TIDY_INVENTORY = new ResourceLocation(Charm.MOD_ID, "server_tidy_inventory");
    public static final ResourceLocation TRIGGER_TIDIED_INVENTORY = new ResourceLocation(Charm.MOD_ID, "tidied_inventory");

    @Override
    public void init() {
        // listen for network requests to run the server callback
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_TIDY_INVENTORY, this::handleServerTidyInventory);

        InventoryTidyingHandler.init();
    }

    private void handleServerTidyInventory(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf data, PacketSender sender) {
        int type = data.readInt();

        server.execute(() -> {
            if (player == null)
                return;

            InventoryTidying.serverCallback(player, type);
        });
    }

    public static void serverCallback(ServerPlayer player, int type) {
        AbstractContainerMenu useContainer;

        if (player.isSpectator())
            return;

        if (type == PLAYER && player.inventoryMenu != null) {
            useContainer = player.inventoryMenu;
        } else if (type == BE && player.containerMenu != null) {
            useContainer = player.containerMenu;
        } else {
            return;
        }

        List<Slot> slots = useContainer.slots;
        slots.stream().findFirst().ifPresent(slot -> {

        });

        boolean hasItemsInInventory = false;

        for (Slot slot : slots) {
            Container inventory = slot.container;

            if (type == PLAYER && slot.container == PlayerHelper.getInventory(player)) {
                InventoryTidyingHandler.sort(PlayerHelper.getInventory(player), 9, 36);
                hasItemsInInventory = !slot.container.isEmpty();
                break;
            } else if (type == BE) {
                InventoryTidyingHandler.sort(inventory, 0, inventory.getContainerSize());
                hasItemsInInventory = !slot.container.isEmpty();
                break;
            }
        }

        if (hasItemsInInventory) {
            // do advancement for tidying inventory
            InventoryTidying.triggerTidiedInventory(player);
        }
    }

    public static void triggerTidiedInventory(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_TIDIED_INVENTORY);
    }
}
