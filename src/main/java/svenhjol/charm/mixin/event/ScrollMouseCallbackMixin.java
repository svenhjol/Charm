package svenhjol.charm.mixin.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.ScrollMouseEvent;

@Environment(EnvType.CLIENT)
@Mixin(MouseHandler.class)
public class ScrollMouseCallbackMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(
        method = "onScroll",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookScroll(long windowIdProbably, double d, double e, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (windowIdProbably == client.getWindow().getWindow()) {
            double direction = (this.minecraft.options.discreteMouseScroll ? Math.signum(e) : e) * this.minecraft.options.mouseWheelSensitivity;
            ScrollMouseEvent.EVENT.invoker().interact(direction);
        }
    }
}
