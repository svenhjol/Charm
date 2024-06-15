package svenhjol.charm.feature.item_tidying.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charm.feature.item_tidying.ItemTidying;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.common.helper.ItemTidyingHelper;

import static svenhjol.charm.feature.item_tidying.common.TidyType.CONTAINER;
import static svenhjol.charm.feature.item_tidying.common.TidyType.PLAYER;

public final class Handlers extends FeatureHolder<ItemTidying> {
    public Handlers(ItemTidying feature) {
        super(feature);
    }

    public void handleTidyInventory(Networking.TidyInventory packet, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (player.isSpectator()) return;

        var type = packet.getType();
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

            if (type == PLAYER && slot.container == serverPlayer.getInventory()) {
                ItemTidyingHelper.sort(player.getInventory(), 9, 36);
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            } else if (type == CONTAINER) {
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
