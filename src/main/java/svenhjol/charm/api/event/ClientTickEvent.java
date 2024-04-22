package svenhjol.charm.api.event;

import net.minecraft.client.Minecraft;

@SuppressWarnings("unused")
public class ClientTickEvent extends CharmEvent<ClientTickEvent.Handler> {
    public static final ClientTickEvent INSTANCE = new ClientTickEvent();

    private ClientTickEvent() {}

    @FunctionalInterface
    public interface Handler {
        void run(Minecraft client);
    }
}
