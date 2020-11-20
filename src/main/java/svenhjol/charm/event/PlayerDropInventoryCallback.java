package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ActionResult;

public interface PlayerDropInventoryCallback {
    Event<PlayerDropInventoryCallback> EVENT = EventFactory.createArrayBacked(PlayerDropInventoryCallback.class, (listeners) -> (player, inventory) -> {
        for (PlayerDropInventoryCallback listener : listeners) {
            ActionResult result = listener.interact(player, inventory);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(PlayerEntity entity, PlayerInventory inventory);
}
