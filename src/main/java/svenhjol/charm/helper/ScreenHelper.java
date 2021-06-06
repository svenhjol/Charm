package svenhjol.charm.helper;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import svenhjol.charm.mixin.accessor.HandledScreenAccessor;

public class ScreenHelper {
    public static int getX(AbstractContainerScreen<?> screen) {
        return ((HandledScreenAccessor)screen).getX();
    }

    public static int getY(AbstractContainerScreen<?> screen) {
        return ((HandledScreenAccessor)screen).getY();
    }
}
