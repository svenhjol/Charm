package svenhjol.charm.mixin.chickens_drop_feathers;

import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.chickens_drop_feathers.ChickensDropFeathers;

@Mixin(Chicken.class)
public class DropFeatherMixin {
    @Shadow public int eggTime;

    /**
     * Defers to tryDropFeather when the vanilla egg-laying ticks are zero.
     */
    @Inject(method = "aiStep", at = @At("RETURN"))
    private void hookTickMovement(CallbackInfo ci) {
        if (this.eggTime <= 1)
            ChickensDropFeathers.tryDropFeather((Chicken)(Object)this);
    }
}