package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.util.ActionResult;

import java.util.function.Consumer;

public interface SetupGuiCallback {
    Event<SetupGuiCallback> EVENT = EventFactory.createArrayBacked(SetupGuiCallback.class, (listeners) -> (client, width, height, addButton) -> {
        for (SetupGuiCallback listener : listeners) {
            ActionResult result = listener.interact(client, width, height, addButton);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(MinecraftClient client, int width, int height, Consumer<AbstractButtonWidget> addButton);
}
