package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public interface PlayerTickEvent {
    Event<PlayerTickEvent> EVENT = EventFactory.createArrayBacked(PlayerTickEvent.class, (listeners) -> (player) -> {
        for (PlayerTickEvent listener : listeners) {
            listener.interact(player);
        }
    });

    void interact(Player player);
}
