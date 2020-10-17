package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public interface LoadWorldCallback {
    Event<LoadWorldCallback> EVENT = EventFactory.createArrayBacked(LoadWorldCallback.class, (listeners) -> (server) -> {
        for (LoadWorldCallback listener : listeners) {
            listener.interact(server);
        }
    });

    void interact(MinecraftServer server);
}
