package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public interface LoadServerFinishEvent {
    Event<LoadServerFinishEvent> EVENT = EventFactory.createArrayBacked(LoadServerFinishEvent.class, (listeners) -> (server) -> {
        for (LoadServerFinishEvent listener : listeners) {
            listener.interact(server);
        }
    });

    void interact(MinecraftServer server);
}
