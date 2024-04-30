package svenhjol.charm.foundation.helper;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

@SuppressWarnings("unused")
public class KeyboardHelper {
    public static boolean isHoldingShift() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, 340) || InputConstants.isKeyDown(handle, 344);
    }
}
