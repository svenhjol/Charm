package svenhjol.charm.mixin.callback;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.EntityKillCallback;

@Mixin(LivingEntity.class)
public class EntityKillCallbackMixin {

    /**
     * Fires the {@link EntityKillCallback} event when an entity is killed.
     */
    @Inject(
        method = "onDeath",
        at = @At("HEAD")
    )
    private void hookOnDeath(DamageSource source, CallbackInfo ci) {
        EntityKillCallback.EVENT.invoker().interact((LivingEntity)(Object)this, source);
    }
}
