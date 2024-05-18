package svenhjol.charm.mixin.feature.spyglass_scope_hiding;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(
        method = "renderSpyglassOverlay",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRenderCameraOverlays(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        ci.cancel();
    }
}
