package svenhjol.charm.mixin.event;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.PlayerTickCallback;

@Mixin(Player.class)
public class PlayerTickMixin {
    /**
     * Fires the {@link PlayerTickCallback} event.
     */
    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    private void hookTick(CallbackInfo ci) {
        PlayerTickCallback.EVENT.invoker().interact((Player)(Object)this);
    }
}
