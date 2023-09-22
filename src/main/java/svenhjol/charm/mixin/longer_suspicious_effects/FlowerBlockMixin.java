package svenhjol.charm.mixin.longer_suspicious_effects;

import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.longer_suspicious_effects.LongerSuspiciousEffects;

import java.util.ArrayList;
import java.util.List;

@Mixin(FlowerBlock.class)
public abstract class FlowerBlockMixin {

    @Shadow public abstract List<SuspiciousEffectHolder.EffectEntry> getSuspiciousEffects();

    @Inject(
        method = "getSuspiciousEffects",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGetSuspiciousEffects(CallbackInfoReturnable<List<SuspiciousEffectHolder.EffectEntry>> cir) {
        boolean anyModified = false;
        List<SuspiciousEffectHolder.EffectEntry> newEntries = new ArrayList<>();

        for (var entry : cir.getReturnValue()) {
            SuspiciousEffectHolder.EffectEntry newEntry;
            var effect = entry.effect();
            if (!effect.isInstantenous() && effect.isBeneficial()) {
                anyModified = true;
                var duration = entry.duration() * LongerSuspiciousEffects.effectMultiplier;
                newEntry = new SuspiciousEffectHolder.EffectEntry(effect, duration);
            } else {
                newEntry = entry;
            }
            newEntries.add(newEntry);
        }

        if (anyModified) {
            cir.setReturnValue(newEntries);
        }
    }
}
