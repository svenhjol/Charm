package svenhjol.charm.mixin.event.block_break_speed_callback;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.foundation.event.BlockBreakSpeedCallback;

@SuppressWarnings("UnreachableCode")
@Mixin(Player.class)
public class PlayerMixin {
    @Inject(
        method = "getDestroySpeed",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGetDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        var player = (Player)(Object)this;
        var currentSpeed = cir.getReturnValue();
        var newSpeed = BlockBreakSpeedCallback.EVENT.invoker().interact(player, state, currentSpeed);
        cir.setReturnValue(newSpeed);
    }
}