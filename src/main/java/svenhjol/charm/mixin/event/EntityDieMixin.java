package svenhjol.charm.mixin.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.EntityDieCallback;

@Mixin(LivingEntity.class)
public class EntityDieMixin {
    /**
     * Fires the {@link EntityDieCallback} event when an entity is killed.
     */
    @Inject(
        method = "die",
        at = @At("HEAD")
    )
    private void hookOnDeath(DamageSource source, CallbackInfo ci) {
        EntityDieCallback.EVENT.invoker().interact((LivingEntity)(Object)this, source);
    }
}
