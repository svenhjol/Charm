package svenhjol.charm.mixin.feature.suspicious_effect_improvements.suspicious_effects_last_longer;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.FlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.suspicious_effect_improvements.suspicious_effects_last_longer.SuspiciousEffectsLastLonger;

@Mixin(FlowerBlock.class)
public abstract class FlowerBlockMixin {

    @ModifyReturnValue(
            method = "getSuspiciousEffects",
            at = @At("RETURN")
    )
    private SuspiciousStewEffects hookGetSuspiciousEffects(SuspiciousStewEffects original) {
        return Resolve.feature(SuspiciousEffectsLastLonger.class).handlers.modifyEffects(original);
    }
}
