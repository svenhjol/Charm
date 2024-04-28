package svenhjol.charm.mixin.feature.silence_microsoft;

import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.silence_microsoft.SilenceMicrosoft;

@Mixin(ClientPacketListener.class)
public class DisableToastMixin {
    @Redirect(
        method = "handleLogin",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/toasts/ToastComponent;addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V"
        )
    )
    private void hookStopNaggingMe(ToastComponent instance, Toast toast) {
        if (SilenceMicrosoft.disableChatMessageNag) return;
        instance.addToast(toast); // Vanilla behavior
    }
}
