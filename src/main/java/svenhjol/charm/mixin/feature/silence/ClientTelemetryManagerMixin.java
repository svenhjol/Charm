package svenhjol.charm.mixin.feature.silence;

import net.minecraft.client.telemetry.ClientTelemetryManager;
import net.minecraft.client.telemetry.TelemetryEventSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.silence.Silence;

@Mixin(ClientTelemetryManager.class)
public class ClientTelemetryManagerMixin {
    @Inject(
        method = "createEventSender",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCreateEventSender(CallbackInfoReturnable<TelemetryEventSender> cir) {
        if (Silence.disableTelemetry()) {
            cir.setReturnValue(TelemetryEventSender.DISABLED);
        }
    }
}
