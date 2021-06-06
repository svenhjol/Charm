package svenhjol.charm.mixin.callback;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.ThrownEntityImpactCallback;

@Mixin(ThrowableProjectile.class)
public class ThrownEntityImpactCallbackMixin {
    /**
     * Fires the {@link ThrownEntityImpactCallback} event when
     * the entity collides with something.
     */
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;onHit(Lnet/minecraft/world/phys/HitResult;)V"
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookTick(CallbackInfo ci, HitResult hitResult) {
        InteractionResult result = ThrownEntityImpactCallback.EVENT.invoker().interact((ThrowableProjectile) (Object) this, hitResult);
        if (result != InteractionResult.PASS)
            ci.cancel();
    }
}
