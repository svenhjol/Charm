package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ServerJoinCallback {
    Event<ServerJoinCallback> EVENT = EventFactory.createArrayBacked(ServerJoinCallback.class, (listeners) -> (playerManager, connection, player) -> {
        for (ServerJoinCallback listener : listeners) {
            listener.interact(playerManager, connection, player);
        }
    });

    void interact(PlayerManager playerManager, ClientConnection connection, ServerPlayerEntity player);
}
