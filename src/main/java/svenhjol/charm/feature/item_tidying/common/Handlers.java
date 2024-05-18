package svenhjol.charm.feature.item_tidying.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charm.feature.item_tidying.ItemTidying;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.ItemTidyingHelper;

public final class Handlers extends FeatureHolder<ItemTidying> {
    public Handlers(ItemTidying feature) {
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
                ItemTidyingHelper.sort(player.getInventory(), 9, 36);
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            } else if (type == TidyType.CONTAINER) {
                ItemTidyingHelper.sort(inventory, 0, inventory.getContainerSize());
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            }
        }

        if (hasItemsInContainer) {
            feature().advancements.tidiedItems(player);
        }
    }
}
