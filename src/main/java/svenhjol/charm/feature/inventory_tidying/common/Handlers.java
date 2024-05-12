package svenhjol.charm.feature.inventory_tidying.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charm.feature.inventory_tidying.InventoryTidying;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.InventoryTidyingHelper;

public final class Handlers extends FeatureHolder<InventoryTidying> {
    public Handlers(InventoryTidying feature) {
        super(feature);
    }

    public void handleTidyInventory(Player player, Networking.C2STidyInventory packet) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (player.isSpectator()) return;

        var type = packet.tidyType();
        AbstractContainerMenu useContainer;

        switch (type) {
            case PLAYER -> useContainer = serverPlayer.inventoryMenu;
            case CONTAINER -> useContainer = serverPlayer.containerMenu;
            default -> {
                return;
            }
        }

        var slots = useContainer.slots;
        var hasItemsInContainer = false;

        for (var slot : slots) {
            var inventory = slot.container;

            if (type == TidyType.PLAYER && slot.container == serverPlayer.getInventory()) {
                InventoryTidyingHelper.sort(player.getInventory(), 9, 36);
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            } else if (type == TidyType.CONTAINER) {
                InventoryTidyingHelper.sort(inventory, 0, inventory.getContainerSize());
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            }
        }

        if (hasItemsInContainer) {
            feature().advancements.tidiedInventory(player);
        }
    }
}
