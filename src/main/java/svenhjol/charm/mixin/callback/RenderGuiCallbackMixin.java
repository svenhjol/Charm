package svenhjol.charm.mixin.callback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.RenderGuiCallback;

@Mixin(HandledScreen.class)
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
            target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground(Lnet/minecraft/client/util/math/MatrixStack;II)V"
        )
    )
    private void hookRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        RenderGuiCallback.EVENT.invoker().interact(client, matrices, mouseX, mouseY, delta);
    }
}
