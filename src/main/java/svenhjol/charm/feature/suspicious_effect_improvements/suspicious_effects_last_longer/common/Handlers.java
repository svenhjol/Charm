package svenhjol.charm.feature.suspicious_effect_improvements.suspicious_effects_last_longer.common;

import net.minecraft.world.effect.MobEffect;
import svenhjol.charm.feature.suspicious_effect_improvements.suspicious_effects_last_longer.SuspiciousEffectsLastLonger;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<SuspiciousEffectsLastLonger> {
    public Handlers(SuspiciousEffectsLastLonger feature) {
        super(feature);
    }

    public int modifyEffect(int originalDuration, MobEffect effect) {
        if (!effect.isInstantenous()) {
            int newDuration;

            if (effect.isBeneficial()) {
                newDuration = originalDuration * feature().beneficialMultiplier();
            } else {
                newDuration = originalDuration * feature().detrimentalMultiplier();
            }
            feature().log().dev(
                    "Old duration was " + originalDuration + ", new duration is " + newDuration +
                        " for effect " + effect.getDescriptionId());

            return newDuration;
        } else {
            return originalDuration;
        }
    }
}
