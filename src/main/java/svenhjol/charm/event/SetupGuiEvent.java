package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratableEntry;
import java.util.List;

public interface SetupGuiEvent {
    Event<SetupGuiEvent> EVENT = EventFactory.createArrayBacked(SetupGuiEvent.class, (listeners) -> (client, width, height, buttons) -> {
        for (SetupGuiEvent listener : listeners) {
            listener.interact(client, width, height, buttons);
        }
    });

    void interact(Minecraft client, int width, int height, List<NarratableEntry> buttons);
}
