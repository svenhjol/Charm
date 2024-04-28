package svenhjol.charm.foundation.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import svenhjol.charm.Charm;
import svenhjol.charm.api.event.ClientEntityJoinEvent;
import svenhjol.charm.api.event.ClientStartEvent;
import svenhjol.charm.foundation.Log;

public final class ClientEvents {
    private static final Log LOGGER = new Log(Charm.ID, "ClientEvents");
    private static boolean initialized = false;

    public static void runOnce() {
        if (initialized) return;

        ClientEntityEvents.ENTITY_LOAD.register(ClientEvents::handleClientEntityLoad);
        ClientLifecycleEvents.CLIENT_STARTED.register(ClientEvents::handleClientStarted);

        initialized = true;
    }

    private static void handleClientEntityLoad(Entity entity, ClientLevel level) {
        ClientEntityJoinEvent.INSTANCE.invoke(entity, level);
    }

    private static void handleClientStarted(Minecraft client) {
        ClientStartEvent.INSTANCE.invoke(client);
    }
}
