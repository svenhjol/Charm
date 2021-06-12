package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;

public interface LoadClientStartCallback {
    Event<LoadClientStartCallback> EVENT = EventFactory.createArrayBacked(LoadClientStartCallback.class, (listeners) -> (client) -> {
        for (LoadClientStartCallback listener : listeners) {
            listener.interact(client);
        }
    });

    void interact(Minecraft server);
}
