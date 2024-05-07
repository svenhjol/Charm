package svenhjol.charm.feature.inventory_tidying;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Network;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class InventoryTidying extends CommonFeature {
    @Override
    public String description() {
        return "Button to automatically tidy inventories.";
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public Optional<Network<? extends Feature>> networking() {
        return Optional.of(new CommonNetworking(this));
    }

    public static void handleTidyInventory(Player player, CommonNetworking.C2STidyInventory packet) {
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
                InventoryTidyingHandler.sort(player.getInventory(), 9, 36);
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            } else if (type == TidyType.CONTAINER) {
                InventoryTidyingHandler.sort(inventory, 0, inventory.getContainerSize());
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            }
        }

        if (hasItemsInContainer) {
            triggerTidiedInventory(player);
        }
    }

    public static void triggerTidiedInventory(Player player) {
        Advancements.trigger(Charm.id("tidied_inventory"), player);
    }
}
