package svenhjol.charm.mixin.chickens_drop_feathers;

import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.ChickensDropFeathers;

@Mixin(ChickenEntity.class)
public class DropFeatherMixin {
    @Shadow public int eggLayTime;

    /**
     * Defers to tryDropFeather when the vanilla egg-laying ticks are zero.
     */
    @Inject(method = "tickMovement", at = @At("RETURN"))
    private void hookTickMovement(CallbackInfo ci) {
        if (this.eggLayTime <= 1)
            ChickensDropFeathers.tryDropFeather((ChickenEntity)(Object)this);
    }
}