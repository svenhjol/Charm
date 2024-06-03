package svenhjol.charm.charmony.common.mixin.event.server_start;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.event.ServerStartEvent;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(
        method = "initServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/server/IntegratedServer;loadLevel()V"
        )
    )
    private void hookInitServerLoadLevel(CallbackInfoReturnable<Boolean> cir) {
        ServerStartEvent.INSTANCE.invoke((MinecraftServer)(Object)this);
    }
}
