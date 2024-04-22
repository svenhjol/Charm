package svenhjol.charm.api.event;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

@SuppressWarnings("unused")
public class ScreenRenderEvent extends CharmEvent<ScreenRenderEvent.Handler> {
    public static final ScreenRenderEvent INSTANCE = new ScreenRenderEvent();

    private ScreenRenderEvent() {}

    @FunctionalInterface
    public interface Handler {
        /**
         * Called on every container screen refresh.
         * @param container Reference to the open container.
         * @param guiGraphics GuiGraphics instance.
         * @param mouseX Mouse X position.
         * @param mouseY Mouse Y position.
         */
        void run(AbstractContainerScreen<?> container, GuiGraphics guiGraphics, int mouseX, int mouseY);
    }
}
