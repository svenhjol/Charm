package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;

public interface ServerWorldInitCallback {
    Event<ServerWorldInitCallback> EVENT = EventFactory.createArrayBacked(ServerWorldInitCallback.class, (listeners) -> world -> {
        for (ServerWorldInitCallback listener : listeners) {
            listener.interact(world);
        }
    });

    void interact(ServerWorld world);
}
