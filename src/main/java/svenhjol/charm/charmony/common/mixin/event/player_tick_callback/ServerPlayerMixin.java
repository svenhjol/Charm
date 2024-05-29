package svenhjol.charm.charmony.common.mixin.event.player_tick_callback;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.callback.PlayerTickCallback;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    /**
     * Fires the {@link PlayerTickCallback} event on the server side.
     */
    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    private void hookTick(CallbackInfo ci) {
        PlayerTickCallback.EVENT.invoker().interact((Player)(Object)this);
    }
}