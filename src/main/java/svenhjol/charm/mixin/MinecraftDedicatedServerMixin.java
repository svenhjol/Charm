package svenhjol.charm.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.LoadWorldCallback;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin {
    @Inject(method = "setupServer", at = @At(
        value = "INVOKE",
        target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V",
        shift = At.Shift.AFTER,
        ordinal = 0,
        remap = false
    ))
    private void hookSetupServer(CallbackInfoReturnable<Boolean> cir) {
        LoadWorldCallback.EVENT.invoker().interact((MinecraftServer)(Object)this);
    }
}
