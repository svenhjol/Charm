package svenhjol.charm.mixin.aerial_affinity_enchantment;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.aerial_affinity_enchantment.AerialAffinityEnchantment;

@Mixin(Player.class)
public class UndoDestroySpeedPenaltyMixin {
    /**
     * Defer to {@link AerialAffinityEnchantment#digFast}.
     * If the check passes and the player is not touching the ground, undo
     * the /= 5.0 dig speed penality that vanilla applies.
     */
    @Inject(
        method = "getDestroySpeed",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGetDestroySpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        Player player = (Player)(Object)this;
        if (!player.isOnGround() && AerialAffinityEnchantment.digFast(player)) {
            Float f = cir.getReturnValue();
            cir.setReturnValue(f * 5.0F);
        }
    }
}
