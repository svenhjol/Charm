package svenhjol.charm.module.inventory_tidying;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.lib.Advancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.inventory_tidying.network.ServerReceiveTidyInventory;

import java.util.List;

import static svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler.BE;
import static svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler.PLAYER;

@CommonModule(mod = Charm.MOD_ID, description = "Button to automatically tidy inventories.")
public class InventoryTidying extends CharmModule {
    public static ServerReceiveTidyInventory SERVER_RECEIVE_TIDY_INVENTORY;
    public static final ResourceLocation TRIGGER_TIDIED_INVENTORY = new ResourceLocation(Charm.MOD_ID, "tidied_inventory");

    @Override
    public void runWhenEnabled() {
        SERVER_RECEIVE_TIDY_INVENTORY = new ServerReceiveTidyInventory();
        InventoryTidyingHandler.init();
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

            if (type == PLAYER && slot.container == player.inventory) {
                InventoryTidyingHandler.sort(player.inventory, 9, 36);
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
        Advancements.triggerActionPerformed(player, TRIGGER_TIDIED_INVENTORY);
    }
}
