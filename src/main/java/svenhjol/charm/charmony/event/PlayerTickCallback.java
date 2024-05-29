package svenhjol.charm.charmony.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

/**
 * A custom Fabric event that is fired every player tick.
 */
public interface PlayerTickCallback {
    Event<PlayerTickCallback> EVENT = EventFactory.createArrayBacked(PlayerTickCallback.class, (listeners) -> (player) -> {
        for (PlayerTickCallback listener : listeners) {
            listener.interact(player);
        }
    });

    void interact(Player player);
}
