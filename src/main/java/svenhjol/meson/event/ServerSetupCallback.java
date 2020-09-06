package svenhjol.meson.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public interface ServerSetupCallback {
    Event<ServerSetupCallback> EVENT = EventFactory.createArrayBacked(ServerSetupCallback.class, (listeners) -> (server) -> {
        for (ServerSetupCallback listener : listeners) {
            listener.interact(server);
        }
    });

    void interact(MinecraftServer server);
}
