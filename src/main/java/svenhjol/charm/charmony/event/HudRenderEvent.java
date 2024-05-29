package svenhjol.charm.charmony.event;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

@SuppressWarnings("unused")
public class HudRenderEvent extends CharmEvent<HudRenderEvent.Handler> {
    public static final HudRenderEvent INSTANCE = new HudRenderEvent();

    private HudRenderEvent() {}

    @FunctionalInterface
    public interface Handler {
        void run(GuiGraphics guiGraphics, DeltaTracker deltaTracker);
    }
}
