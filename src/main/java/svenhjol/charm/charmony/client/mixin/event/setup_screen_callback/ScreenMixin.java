package svenhjol.charm.charmony.client.mixin.event.setup_screen_callback;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.client.event.SetupScreenCallback;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    /**
     * Fires the {@link SetupScreenCallback} event.
     * We can access existing GUI buttons and add new ones as required using the event invoked in this hook.
     */
    @Inject(
        method = "init(Lnet/minecraft/client/Minecraft;II)V",
        at = @At("RETURN")
    )
    private void hookInit(Minecraft client, int width, int height, CallbackInfo ci) {
        SetupScreenCallback.EVENT.invoker().interact((Screen)(Object)this);
    }

    /**
     * Also call setup callback when the screen is resized.
     */
    @Inject(
        method = "rebuildWidgets",
        at = @At("RETURN")
    )
    private void hookRebuildWidgets(CallbackInfo ci) {
        SetupScreenCallback.EVENT.invoker().interact((Screen)(Object)this);
    }
}
