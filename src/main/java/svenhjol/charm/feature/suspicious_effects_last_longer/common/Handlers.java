package svenhjol.charm.feature.suspicious_effects_last_longer.common;

import net.minecraft.world.item.component.SuspiciousStewEffects;
import svenhjol.charm.feature.suspicious_effects_last_longer.SuspiciousEffectsLastLonger;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.ArrayList;
import java.util.List;

public final class Handlers extends FeatureHolder<SuspiciousEffectsLastLonger> {
    public Handlers(SuspiciousEffectsLastLonger feature) {
        super(feature);
    }

    public SuspiciousStewEffects modifyEffects(SuspiciousStewEffects effects) {
        List<SuspiciousStewEffects.Entry> newEntries = new ArrayList<>();

        for (SuspiciousStewEffects.Entry entry : effects.effects()) {
            SuspiciousStewEffects.Entry newEntry;
            var holder = entry.effect();
            var effect = holder.value();

            if (!effect.isInstantenous()) {
                int newDuration;
                int oldDuration = entry.duration();

                if (effect.isBeneficial()) {
                    newDuration = entry.duration() * SuspiciousEffectsLastLonger.beneficialMultiplier;
                } else {
                    newDuration = entry.duration() * SuspiciousEffectsLastLonger.detrimentalMultiplier;
                }
                feature().log().dev(
                    "Old duration was " + oldDuration + ", new duration is " + newDuration +
                        " for effect " + effect.getDescriptionId());
                newEntry = new SuspiciousStewEffects.Entry(holder, newDuration);
            } else {
                newEntry = entry;
            }

            newEntries.add(newEntry);
        }

        return new SuspiciousStewEffects(newEntries);
    }
}
