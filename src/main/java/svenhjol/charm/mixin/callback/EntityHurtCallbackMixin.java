package svenhjol.charm.mixin.callback;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.EntityHurtCallback;

@Mixin(LivingEntity.class)
public class EntityHurtCallbackMixin {

    /**
     * Fires the {@link EntityHurtCallback} when entity is hurt.
     *
     * Cancellable with ActionResult == FAIL.
     */
    @Inject(
        method = "applyDamage",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
        ActionResult result = EntityHurtCallback.EVENT.invoker().interact((LivingEntity) (Object) this, source, amount);
        if (result == ActionResult.FAIL)
            ci.cancel();
    }
}
