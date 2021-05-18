package svenhjol.charm.mixin.callback;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.ServerJoinCallback;

@Mixin(PlayerManager.class)
public class ServerJoinCallbackMixin {

    /**
     * Fires the {@link ServerJoinCallback} event.
     */
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void hookOnPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        ServerJoinCallback.EVENT.invoker().interact((PlayerManager)(Object)this, connection, player);
    }
}
