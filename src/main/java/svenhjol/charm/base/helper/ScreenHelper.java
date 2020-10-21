package svenhjol.charm.base.helper;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import svenhjol.charm.mixin.accessor.HandledScreenAccessor;

public class ScreenHelper {
    public static int getX(HandledScreen<?> screen) {
        return ((HandledScreenAccessor)screen).getX();
    }

    public static int getY(HandledScreen<?> screen) {
        return ((HandledScreenAccessor)screen).getY();
    }
}
