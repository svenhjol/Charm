package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;

public interface LoadClientStartEvent {
    Event<LoadClientStartEvent> EVENT = EventFactory.createArrayBacked(LoadClientStartEvent.class, (listeners) -> (client) -> {
        for (LoadClientStartEvent listener : listeners) {
            listener.interact(client);
        }
    });

    void interact(Minecraft server);
}
