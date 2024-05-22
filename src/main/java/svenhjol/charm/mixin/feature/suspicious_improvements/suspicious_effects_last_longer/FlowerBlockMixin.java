package svenhjol.charm.mixin.feature.suspicious_improvements.suspicious_effects_last_longer;

import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.FlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.suspicious_improvements.suspicious_effects_last_longer.SuspiciousEffectsLastLonger;
import svenhjol.charm.foundation.Resolve;

@Mixin(FlowerBlock.class)
public abstract class FlowerBlockMixin {

    @Inject(
        method = "getSuspiciousEffects",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGetSuspiciousEffects(CallbackInfoReturnable<SuspiciousStewEffects> cir) {
        var effects = cir.getReturnValue();
        var modified = Resolve.feature(SuspiciousEffectsLastLonger.class).handlers.modifyEffects(effects);
        cir.setReturnValue(modified);
    }
}
