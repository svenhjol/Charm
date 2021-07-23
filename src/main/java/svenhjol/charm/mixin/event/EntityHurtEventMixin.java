package svenhjol.charm.mixin.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.EntityHurtEvent;

@Mixin(LivingEntity.class)
public class EntityHurtEventMixin {
    /**
     * Fires the {@link EntityHurtEvent} when entity is hurt.
     *
     * Cancellable with ActionResult == FAIL.
     */
    @Inject(
        method = "actuallyHurt",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
        InteractionResult result = EntityHurtEvent.EVENT.invoker().interact((LivingEntity) (Object) this, source, amount);
        if (result == InteractionResult.FAIL)
            ci.cancel();
    }
}
