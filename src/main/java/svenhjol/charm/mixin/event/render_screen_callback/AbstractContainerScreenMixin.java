package svenhjol.charm.mixin.event.render_screen_callback;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.client.event.RenderScreenCallback;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    /**
     * Fires the {@link RenderScreenCallback} event.
     * This is called on every vanilla drawForeground tick and allows custom gui rendering.
     */
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lnet/minecraft/client/gui/GuiGraphics;II)V"
        )
    )
    private void hookRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        RenderScreenCallback.EVENT.invoker().interact((AbstractContainerScreen<?>)(Object)this, guiGraphics, mouseX, mouseY);
    }
}
