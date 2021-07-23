package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ScrollMouseEvent {
    Event<ScrollMouseEvent> EVENT = EventFactory.createArrayBacked(ScrollMouseEvent.class, (listeners) -> (direction) -> {
        for (ScrollMouseEvent listener : listeners) {
            listener.interact(direction);
        }
    });

    void interact(double direction);
}
