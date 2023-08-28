package svenhjol.charm.mixin.extra_mob_drops;

import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.extra_mob_drops.ExtraMobDrops;

@Mixin(Chicken.class)
public class ChickenMixin {
    @Shadow
    public int eggTime;

    @Inject(
        method = "aiStep",
        at = @At("RETURN")
    )
    private void hookTickMovement(CallbackInfo ci) {
        if (this.eggTime <= 1) {
            ExtraMobDrops.tryDropFeather((Chicken)(Object)this);
        }
    }
}
