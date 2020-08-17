package svenhjol.charm.mixin;

import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.ChickensDropFeathers;

@Mixin(ChickenEntity.class)
public class ChickenEntityMixin {
    @Shadow public int eggLayTime;

    @Inject(method = "tickMovement", at = @At("RETURN"))
    private void hookTickMovement(CallbackInfo ci) {
        if (this.eggLayTime <= 1)
            ChickensDropFeathers.tryDropFeather((ChickenEntity)(Object)this);
    }
}