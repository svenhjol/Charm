package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface CommonSetupCallback {
    Event<CommonSetupCallback> EVENT = EventFactory.createArrayBacked(CommonSetupCallback.class, (listeners) -> () -> {
        for (CommonSetupCallback listener : listeners) {
            listener.interact();
        }
    });

    void interact();
}
