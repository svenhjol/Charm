package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public interface PlayerTickCallback {
    Event<PlayerTickCallback> EVENT = EventFactory.createArrayBacked(PlayerTickCallback.class, (listeners) -> (player) -> {
        for (PlayerTickCallback listener : listeners) {
            listener.interact(player);
        }
    });

    void interact(Player player);
}
