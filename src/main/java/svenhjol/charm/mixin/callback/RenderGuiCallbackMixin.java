package svenhjol.charm.mixin.callback;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.RenderGuiCallback;

@Mixin(AbstractContainerScreen.class)
public class RenderGuiCallbackMixin {
    /**
     * Fires the {@link RenderGuiCallback} event.
     *
     * This is called on every vanilla drawForeground tick
     * and allows custom gui rendering.
     *
     * Simulates Forge's GuiContainerEvent.DrawForeground
     */
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lcom/mojang/blaze3d/vertex/PoseStack;II)V"
        )
    )
    private void hookRender(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        RenderGuiCallback.EVENT.invoker().interact(client, matrices, mouseX, mouseY, delta);
    }
}
