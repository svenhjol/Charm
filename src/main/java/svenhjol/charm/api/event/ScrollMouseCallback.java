package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ScrollMouseCallback {
    Event<ScrollMouseCallback> EVENT = EventFactory.createArrayBacked(ScrollMouseCallback.class, (listeners) -> (direction) -> {
        for (ScrollMouseCallback listener : listeners) {
            listener.interact(direction);
        }
    });

    void interact(double direction);
}
