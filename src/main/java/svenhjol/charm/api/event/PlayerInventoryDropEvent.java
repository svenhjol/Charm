package svenhjol.charm.api.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("unused")
public class PlayerInventoryDropEvent extends CharmEvent<PlayerInventoryDropEvent.Handler> {
    public static final PlayerInventoryDropEvent INSTANCE = new PlayerInventoryDropEvent();

    private PlayerInventoryDropEvent() {}

    public InteractionResult invoke(Player player, Inventory inventory) {
        for (var handler : getHandlers()) {
            var result = handler.run(player, inventory);

            // Return the first handler that satisfies the itemstack hover.
            if (result == InteractionResult.SUCCESS) {
                return result;
            }
        }

        return InteractionResult.PASS;
    }

    @FunctionalInterface
    public interface Handler {
        InteractionResult run(Player player, Inventory inventory);
    }
}
