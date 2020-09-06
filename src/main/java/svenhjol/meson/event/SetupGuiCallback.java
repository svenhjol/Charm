package svenhjol.meson.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;

import java.util.List;
import java.util.function.Consumer;

public interface SetupGuiCallback {
    Event<SetupGuiCallback> EVENT = EventFactory.createArrayBacked(SetupGuiCallback.class, (listeners) -> (client, width, height, buttons, addButton) -> {
        for (SetupGuiCallback listener : listeners) {
            listener.interact(client, width, height, buttons, addButton);
        }
    });

    void interact(MinecraftClient client, int width, int height, List<AbstractButtonWidget> buttons, Consumer<AbstractButtonWidget> addButton);
}
