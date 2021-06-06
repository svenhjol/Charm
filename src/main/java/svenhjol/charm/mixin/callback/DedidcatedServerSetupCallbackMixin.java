package svenhjol.charm.mixin.callback;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.DedicatedServerSetupCallback;

@Mixin(DedicatedServer.class)
public class DedidcatedServerSetupCallbackMixin {

    /**
     * Fires the {@link DedicatedServerSetupCallback} event.
     *
     * This can be used by any Charm module to perform init when
     * the dedicated server starts.
     */
    @Inject(method = "setupServer", at = @At(
        value = "INVOKE",
        target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V",
        shift = At.Shift.AFTER,
        ordinal = 0,
        remap = false
    ))
    private void hookSetupServer(CallbackInfoReturnable<Boolean> cir) {
        DedicatedServerSetupCallback.EVENT.invoker().interact((MinecraftServer)(Object)this);
    }
}
