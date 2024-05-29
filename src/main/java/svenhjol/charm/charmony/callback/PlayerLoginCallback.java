package svenhjol.charm.charmony.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

/**
 * A custom Fabric event that is fired whenever a player logs in.
 */
public interface PlayerLoginCallback {
    Event<PlayerLoginCallback> EVENT = EventFactory.createArrayBacked(PlayerLoginCallback.class, listeners -> player -> {
        for (PlayerLoginCallback listener : listeners) {
            listener.interact(player);
        }
    });

    void interact(Player player);
}
