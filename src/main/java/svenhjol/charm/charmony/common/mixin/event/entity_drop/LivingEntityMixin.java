package svenhjol.charm.charmony.common.mixin.event.entity_drop;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.event.EntityKilledDropEvent;

@SuppressWarnings({"ConstantConditions", "UnreachableCode"})
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    /**
     * Fires the {@link EntityKilledDropEvent} event.
     */
    @Inject(
        method = "dropAllDeathLoot",
        at = @At("TAIL")
    )
    private void hookDrop(DamageSource damageSource, CallbackInfo ci) {
        var entity = (LivingEntity)(Object)this;
        
        EntityKilledDropEvent.INSTANCE.invoke(entity, damageSource);
    }
}