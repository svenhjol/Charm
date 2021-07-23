package svenhjol.charm.mixin.event;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.PlayerTickEvent;

@Mixin(Player.class)
public class PlayerTickEventMixin {

    /**
     * Fires the {@link PlayerTickEvent} event.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void hookTick(CallbackInfo ci) {
        PlayerTickEvent.EVENT.invoker().interact((Player)(Object)this);
    }
}
