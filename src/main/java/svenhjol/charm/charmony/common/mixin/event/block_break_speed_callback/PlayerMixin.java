package svenhjol.charm.charmony.common.mixin.event.block_break_speed_callback;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.callback.BlockBreakSpeedCallback;

@SuppressWarnings("UnreachableCode")
@Mixin(Player.class)
public class PlayerMixin {

    @ModifyReturnValue(
            method = "getDestroySpeed",
            at = @At("RETURN")
    )
    private float hookGetDestroySpeed(float original, @Local(argsOnly = true) BlockState state) {
        var player = (Player)(Object)this;
        return BlockBreakSpeedCallback.EVENT.invoker().interact(player, state, original);
    }
}