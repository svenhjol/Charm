package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface ClientPlayerJoinCallback {
    Event<ClientPlayerJoinCallback> EVENT = EventFactory.createArrayBacked(ClientPlayerJoinCallback.class, (listeners) -> (client) -> {
        for (ClientPlayerJoinCallback listener : listeners) {
            listener.interact(client);
        }
    });

    void interact(MinecraftClient client);
}
