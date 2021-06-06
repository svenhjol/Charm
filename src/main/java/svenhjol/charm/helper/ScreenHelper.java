package svenhjol.charm.helper;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import svenhjol.charm.mixin.accessor.AbstractContainerScreenAccessor;

public class ScreenHelper {
    public static int getX(AbstractContainerScreen<?> screen) {
        return ((AbstractContainerScreenAccessor)screen).getX();
    }

    public static int getY(AbstractContainerScreen<?> screen) {
        return ((AbstractContainerScreenAccessor)screen).getY();
    }
}
