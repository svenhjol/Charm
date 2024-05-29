package svenhjol.charm.charmony.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;

/**
 * A custom Fabric event that is triggered when a new screen is initialized.
 */
public interface SetupScreenCallback {
    Event<SetupScreenCallback> EVENT = EventFactory.createArrayBacked(SetupScreenCallback.class, listeners -> screen -> {
        for (SetupScreenCallback listener : listeners) {
            listener.interact(screen);
        }
    });

    void interact(Screen screen);
}
