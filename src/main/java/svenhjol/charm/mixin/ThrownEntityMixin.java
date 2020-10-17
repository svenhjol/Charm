package svenhjol.charm.mixin;

import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.ThrownEntityImpactCallback;

@Mixin(ThrownEntity.class)
public class ThrownEntityMixin {
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;onCollision(Lnet/minecraft/util/hit/HitResult;)V"
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookTick(CallbackInfo ci, HitResult hitResult) {
        ActionResult result = ThrownEntityImpactCallback.EVENT.invoker().interact((ThrownEntity) (Object) this, hitResult);
        if (result != ActionResult.PASS)
            ci.cancel();
    }
}
