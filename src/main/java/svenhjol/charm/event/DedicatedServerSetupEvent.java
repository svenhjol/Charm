package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public interface DedicatedServerSetupEvent {
    Event<DedicatedServerSetupEvent> EVENT = EventFactory.createArrayBacked(DedicatedServerSetupEvent.class, (listeners) -> (server) -> {
        for (DedicatedServerSetupEvent listener : listeners) {
            listener.interact(server);
        }
    });

    void interact(MinecraftServer server);
}
