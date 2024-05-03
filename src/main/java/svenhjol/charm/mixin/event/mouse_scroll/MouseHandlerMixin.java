package svenhjol.charm.mixin.event.mouse_scroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.api.event.MouseScrollEvent;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
        method = "onScroll",
        at = @At("HEAD")
    )
    private void hookScroll(long windowId, double d, double e, CallbackInfo ci) {
        var client = Minecraft.getInstance();
        if (windowId == client.getWindow().getWindow()) {
            var direction = (minecraft.options.discreteMouseScroll().get() ? Math.signum(e) : e) * minecraft.options.mouseWheelSensitivity().get();
            if (minecraft.getOverlay() == null && minecraft.screen != null) {
                MouseScrollEvent.ON_SCREEN.invoke(direction);
            } else if (minecraft.player != null) {
                MouseScrollEvent.OFF_SCREEN.invoke(direction);
            }
        }
    }
}
