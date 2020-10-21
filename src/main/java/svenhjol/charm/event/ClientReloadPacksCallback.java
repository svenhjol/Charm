package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface ClientReloadPacksCallback {
    Event<ClientReloadPacksCallback> EVENT = EventFactory.createArrayBacked(ClientReloadPacksCallback.class, (listeners) -> (client) -> {
        for (ClientReloadPacksCallback listener : listeners) {
            listener.interact(client);
        }
    });

    void interact(MinecraftClient client);
}
