package svenhjol.charm.mixin.event.entity_tick_callback;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.common.event.EntityTickCallback;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
        method = "aiStep",
        at = @At("TAIL")
    )
    private void hookAiStep(CallbackInfo ci) {
        EntityTickCallback.EVENT.invoker().interact((LivingEntity)(Object)this);
    }
}
