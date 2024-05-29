package svenhjol.charm.feature.suspicious_effect_improvements;

import svenhjol.charm.feature.suspicious_effect_improvements.suspicious_big_plants.SuspiciousBigPlants;
import svenhjol.charm.feature.suspicious_effect_improvements.suspicious_effects_last_longer.SuspiciousEffectsLastLonger;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

import java.util.List;

@Feature(description = "Adds more functionality to suspicious effects, flowers and stews.")
public final class SuspiciousEffectImprovements extends CommonFeature {
    public SuspiciousEffectImprovements(CommonLoader loader) {
        super(loader);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.charmony.Feature>> children() {
        return List.of(
            new SuspiciousBigPlants(loader()),
            new SuspiciousEffectsLastLonger(loader())
        );
    }
}
