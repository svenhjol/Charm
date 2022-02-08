package svenhjol.charm.helper;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class KeyboardHelper {
    public static boolean isShiftClick() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, 340) || InputConstants.isKeyDown(handle, 344);
    }
}
