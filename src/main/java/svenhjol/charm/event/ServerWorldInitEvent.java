package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;

public interface ServerWorldInitEvent {
    Event<ServerWorldInitEvent> EVENT = EventFactory.createArrayBacked(ServerWorldInitEvent.class, (listeners) -> world -> {
        for (ServerWorldInitEvent listener : listeners) {
            listener.interact(world);
        }
    });

    void interact(ServerLevel world);
}
