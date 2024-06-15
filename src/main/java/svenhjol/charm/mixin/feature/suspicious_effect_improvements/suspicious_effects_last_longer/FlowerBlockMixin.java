package svenhjol.charm.mixin.feature.suspicious_effect_improvements.suspicious_effects_last_longer;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.FlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.suspicious_effect_improvements.suspicious_effects_last_longer.SuspiciousEffectsLastLonger;

@Mixin(FlowerBlock.class)
public abstract class FlowerBlockMixin {

    @Shadow public abstract MobEffect getSuspiciousEffect();

    @ModifyReturnValue(
            method = "getEffectDuration",
            at = @At("RETURN")
    )
    private int hookGetEffectDuration(int original) {
        var effect = getSuspiciousEffect();
        return Resolve.feature(SuspiciousEffectsLastLonger.class).handlers.modifyEffect(original, effect);
    }
}
