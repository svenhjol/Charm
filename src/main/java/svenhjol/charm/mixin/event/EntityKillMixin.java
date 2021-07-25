package svenhjol.charm.mixin.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.EntityKillCallback;

@Mixin(LivingEntity.class)
public class EntityKillMixin {
    /**
     * Fires the {@link EntityKillCallback} event when an entity is killed.
     */
    @Inject(
        method = "die",
        at = @At("HEAD")
    )
    private void hookOnDeath(DamageSource source, CallbackInfo ci) {
        EntityKillCallback.EVENT.invoker().interact((LivingEntity)(Object)this, source);
    }
}
