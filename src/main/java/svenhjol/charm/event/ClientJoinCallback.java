package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface ClientJoinCallback {
    Event<ClientJoinCallback> EVENT = EventFactory.createArrayBacked(ClientJoinCallback.class, (listeners) -> (client) -> {
        for (ClientJoinCallback listener : listeners) {
            listener.interact(client);
        }
    });

    void interact(MinecraftClient client);
}
