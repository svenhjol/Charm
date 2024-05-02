package svenhjol.charm.feature.inventory_tidying;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Networking;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class InventoryTidying extends CommonFeature {
    @Override
    public String description() {
        return "Button to automatically tidy inventories.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public Optional<Networking<? extends Feature>> networking() {
        return Optional.of(new CommonNetworking(this));
    }

    public static void handleTidyInventory(Player player, CommonNetworking.C2STidyInventory message) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (player.isSpectator()) return;

        var type = message.tidyType();
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
        Advancements.trigger(new ResourceLocation(Charm.ID, "tidied_inventory"), player);
    }
}
