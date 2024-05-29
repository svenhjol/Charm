package svenhjol.charm.charmony.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * A custom Fabric event that fires on every screen render.
 */
public interface RenderScreenCallback {
    Event<RenderScreenCallback> EVENT = EventFactory.createArrayBacked(RenderScreenCallback.class, listeners -> (container, guiGraphics, mouseX, mouseY) -> {
        for (RenderScreenCallback listener : listeners) {
            listener.interact(container, guiGraphics, mouseX, mouseY);
        }
    });

    void interact(AbstractContainerScreen<?> container, GuiGraphics guiGraphics, int mouseX, int mouseY);
}
