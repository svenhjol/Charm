package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public interface LoadServerFinishCallback {
    Event<LoadServerFinishCallback> EVENT = EventFactory.createArrayBacked(LoadServerFinishCallback.class, (listeners) -> (server) -> {
        for (LoadServerFinishCallback listener : listeners) {
            listener.interact(server);
        }
    });

    void interact(MinecraftServer server);
}
