package svenhjol.charm.mixin.event;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.ServerJoinEvent;

@Mixin(PlayerList.class)
public class ServerJoinCallbackMixin {
    /**
     * Fires the {@link ServerJoinEvent} event.
     */
    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    private void hookOnPlayerConnect(Connection connection, ServerPlayer player, CallbackInfo ci) {
        ServerJoinEvent.EVENT.invoker().interact((PlayerList)(Object)this, connection, player);
    }
}
