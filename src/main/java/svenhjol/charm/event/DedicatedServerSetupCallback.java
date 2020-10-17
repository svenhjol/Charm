package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public interface DedicatedServerSetupCallback {
    Event<DedicatedServerSetupCallback> EVENT = EventFactory.createArrayBacked(DedicatedServerSetupCallback.class, (listeners) -> (server) -> {
        for (DedicatedServerSetupCallback listener : listeners) {
            listener.interact(server);
        }
    });

    void interact(MinecraftServer server);
}
