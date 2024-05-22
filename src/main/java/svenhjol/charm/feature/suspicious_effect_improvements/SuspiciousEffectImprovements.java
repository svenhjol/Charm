package svenhjol.charm.feature.suspicious_effect_improvements;

import svenhjol.charm.feature.suspicious_effect_improvements.suspicious_big_plants.SuspiciousBigPlants;
import svenhjol.charm.feature.suspicious_effect_improvements.suspicious_effects_last_longer.SuspiciousEffectsLastLonger;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.List;

@Feature(description = "Adds more functionality to suspicious effects, flowers and stews.")
public final class SuspiciousEffectImprovements extends CommonFeature {
    public SuspiciousEffectImprovements(CommonLoader loader) {
        super(loader);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.foundation.Feature>> children() {
        return List.of(
            new SuspiciousBigPlants(loader()),
            new SuspiciousEffectsLastLonger(loader())
        );
    }
}
