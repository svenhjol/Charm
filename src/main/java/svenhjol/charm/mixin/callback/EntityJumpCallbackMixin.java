package svenhjol.charm.mixin.callback;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.EntityJumpCallback;

@Mixin(LivingEntity.class)
public class EntityJumpCallbackMixin {

    /**
     * Fires the {@link EntityJumpCallback} event when the entity jumps.
     */
    @Inject(
        method = "jump",
        at = @At("TAIL")
    )
    private void hookJump(CallbackInfo ci) {
        EntityJumpCallback.EVENT.invoker().interact((LivingEntity)(Object)this);
    }
}
