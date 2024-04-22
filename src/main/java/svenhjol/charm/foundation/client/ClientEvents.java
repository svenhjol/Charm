package svenhjol.charm.foundation.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import svenhjol.charm.api.event.ClientStartEvent;

public final class ClientEvents {
    private static boolean initialized = false;

    public static void runOnce() {
        if (initialized) return;

        ClientLifecycleEvents.CLIENT_STARTED.register(ClientEvents::handleClientStarted);

        initialized = true;
    }

    private static void handleClientStarted(Minecraft client) {
        ClientStartEvent.INSTANCE.invoke(client);
    }
}
