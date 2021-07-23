package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

public interface ServerJoinEvent {
    Event<ServerJoinEvent> EVENT = EventFactory.createArrayBacked(ServerJoinEvent.class, (listeners) -> (playerManager, connection, player) -> {
        for (ServerJoinEvent listener : listeners) {
            listener.interact(playerManager, connection, player);
        }
    });

    void interact(PlayerList playerManager, Connection connection, ServerPlayer player);
}
