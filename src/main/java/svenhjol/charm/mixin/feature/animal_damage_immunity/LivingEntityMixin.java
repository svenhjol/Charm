package svenhjol.charm.mixin.feature.animal_damage_immunity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.animal_damage_immunity.AnimalDamageImmunity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
        method = "hurt",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        var shouldHurt = Resolve.feature(AnimalDamageImmunity.class).handlers.shouldHurt((LivingEntity)(Object)this, damageSource);
        if (!shouldHurt) {
            cir.setReturnValue(false);
        }
    }
}
