package svenhjol.charm.feature.suspicious_improvements.suspicious_big_plants;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.suspicious_improvements.SuspiciousImprovements;
import svenhjol.charm.feature.suspicious_improvements.suspicious_effects_last_longer.SuspiciousEffectsLastLonger;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(description = "Suspicious stews can be crafted from pitcher plants and sunflowers.")
public final class SuspiciousBigPlants extends CommonFeature implements ChildFeature<SuspiciousImprovements> {
    @Configurable(name = "Pitcher plant effect duration", description = "Number of seconds of strength and regeneration from a pitcher plant.")
    private static int pitcherPlantEffectDuration = 8;

    @Configurable(name = "Sunflower effect duration", description = "Number of seconds of health boost from a sunflower.")
    private static int sunflowerEffectDuration = 8;

    public SuspiciousBigPlants(CommonLoader loader) {
        super(loader);
    }

    @Override
    public Class<SuspiciousImprovements> typeForParent() {
        return SuspiciousImprovements.class;
    }

    public int pitcherPlantEffectDuration() {
        return isEnabled() ? Mth.clamp(pitcherPlantEffectDuration, 0, 1000)
            * Resolve.feature(SuspiciousEffectsLastLonger.class).beneficialMultiplier() : 0;
    }

    public int sunflowerEffectDuration() {
        return isEnabled() ? Mth.clamp(sunflowerEffectDuration, 0, 1000)
            * Resolve.feature(SuspiciousEffectsLastLonger.class).beneficialMultiplier() : 0;
    }
}
