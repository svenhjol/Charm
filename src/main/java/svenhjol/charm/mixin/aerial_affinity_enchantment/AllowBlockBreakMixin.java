package svenhjol.charm.mixin.aerial_affinity_enchantment;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.aerial_affinity_enchantment.AerialAffinityEnchantment;

@Mixin(Player.class)
public class AllowBlockBreakMixin {

    // This is broken in Loom 0.8-SNAPSHOT because onGround doesn't obfuscate when in prod.
    // Use the inject/cancel version until it is fixed.
//    @Redirect(
//        method = "getBlockBreakingSpeed",
//        at = @At(
//            value = "FIELD",
//            target = "Lnet/minecraft/entity/player/PlayerEntity;onGround:Z"
//        )
//    )
//    private boolean hookDigSpeedOnGround(PlayerEntity player, BlockState state) {
//        return player.isOnGround() || AerialAffinityEnchantment.digFast(player);
//    }


    /**
     * Defer to digFast.
     * If the check passes and the player is not touching the ground, undo
     * the /= 5.0 dig speed penality that vanilla applies.
     */
    @Inject(
        method = "getBlockBreakingSpeed",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookDigSpeedOnGround(BlockState block, CallbackInfoReturnable<Float> cir) {
        Player player = (Player)(Object)this;
        if (!player.isOnGround() && AerialAffinityEnchantment.digFast(player)) {
            Float f = cir.getReturnValue();
            cir.setReturnValue(f * 5.0F);
        }
    }
}
