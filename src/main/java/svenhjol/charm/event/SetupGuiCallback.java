package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.List;
import java.util.function.Consumer;

public interface SetupGuiCallback {
    Event<SetupGuiCallback> EVENT = EventFactory.createArrayBacked(SetupGuiCallback.class, (listeners) -> (client, width, height, buttons) -> {
        for (SetupGuiCallback listener : listeners) {
            listener.interact(client, width, height, buttons);
        }
    });

    void interact(MinecraftClient client, int width, int height, List<ClickableWidget> buttons);
}
