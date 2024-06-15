package svenhjol.charm.charmony.helper;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

public class ScreenHelper {
    public ScreenHelper() {
    }

    public static <T extends GuiEventListener> T addRenderableWidget(Screen screen, T guiEventListener) {
        screen.renderables.add((Renderable)guiEventListener);
        screen.children.add(guiEventListener);
        screen.narratables.add((NarratableEntry)guiEventListener);
        return guiEventListener;
    }
}
