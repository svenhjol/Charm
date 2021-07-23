package svenhjol.charm.mixin.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.EntityKillEvent;

@Mixin(LivingEntity.class)
public class EntityKillEventMixin {
    /**
     * Fires the {@link EntityKillEvent} event when an entity is killed.
     */
    @Inject(
        method = "die",
        at = @At("HEAD")
    )
    private void hookOnDeath(DamageSource source, CallbackInfo ci) {
        EntityKillEvent.EVENT.invoker().interact((LivingEntity)(Object)this, source);
    }
}
