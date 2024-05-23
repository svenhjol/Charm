package svenhjol.charm.mixin.event.entity_hurt;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.api.event.EntityHurtEvent;

@SuppressWarnings("UnreachableCode")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
        method = "actuallyHurt",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
        var result = EntityHurtEvent.INSTANCE.invoke((LivingEntity) (Object) this, source, amount);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
