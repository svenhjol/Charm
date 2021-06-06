package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

public interface ServerJoinCallback {
    Event<ServerJoinCallback> EVENT = EventFactory.createArrayBacked(ServerJoinCallback.class, (listeners) -> (playerManager, connection, player) -> {
        for (ServerJoinCallback listener : listeners) {
            listener.interact(playerManager, connection, player);
        }
    });

    void interact(PlayerList playerManager, Connection connection, ServerPlayer player);
}
