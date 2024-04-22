package svenhjol.charm.api.event;

import net.minecraft.client.Minecraft;

@SuppressWarnings("unused")
public class ClientStartEvent extends CharmEvent<ClientStartEvent.Handler> {
    public static final ClientStartEvent INSTANCE = new ClientStartEvent();

    private ClientStartEvent() {}

    public void invoke(Minecraft client) {
        for (var handler : getHandlers()) {
            handler.run(client);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(Minecraft client);
    }
}
