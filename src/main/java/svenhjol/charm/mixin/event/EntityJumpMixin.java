package svenhjol.charm.mixin.event;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.EntityJumpCallback;

@Mixin(LivingEntity.class)
public class EntityJumpMixin {

    /**
     * Fires the {@link EntityJumpCallback} event when the entity jumps.
     */
    @Inject(
        method = "jumpFromGround",
        at = @At("TAIL")
    )
    private void hookJump(CallbackInfo ci) {
        EntityJumpCallback.EVENT.invoker().interact((LivingEntity)(Object)this);
    }
}
