package svenhjol.charm.mixin.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.api.event.PlayerDieCallback;

@Mixin(Player.class)
public class PlayerDieMixin {
    /**
     * Fires the {@link PlayerDieCallback} event when the player is killed.
     * This is a server-side event.
     */
    @Inject(
        method = "die",
        at = @At("HEAD")
    )
    private void hookOnDeath(DamageSource source, CallbackInfo ci) {
        PlayerDieCallback.EVENT.invoker().interact((Player)(Object)this, source);
    }
}
