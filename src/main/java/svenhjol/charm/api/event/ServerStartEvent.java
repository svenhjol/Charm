package svenhjol.charm.api.event;

import net.minecraft.server.MinecraftServer;

/**
 * A simple implementation of Forge's ServerAboutToStartEvent.
 */
public class ServerStartEvent extends CharmEvent<ServerStartEvent.Handler> {
    public static final ServerStartEvent INSTANCE = new ServerStartEvent();

    public void invoke(MinecraftServer server) {
        for (Handler handler : INSTANCE.getHandlers()) {
            handler.run(server);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(MinecraftServer server);
    }
}
