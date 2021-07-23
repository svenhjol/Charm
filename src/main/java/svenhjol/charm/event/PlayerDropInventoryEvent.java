package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public interface PlayerDropInventoryEvent {
    Event<PlayerDropInventoryEvent> EVENT = EventFactory.createArrayBacked(PlayerDropInventoryEvent.class, (listeners) -> (player, inventory) -> {
        for (PlayerDropInventoryEvent listener : listeners) {
            InteractionResult result = listener.interact(player, inventory);
            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(Player entity, Inventory inventory);
}
