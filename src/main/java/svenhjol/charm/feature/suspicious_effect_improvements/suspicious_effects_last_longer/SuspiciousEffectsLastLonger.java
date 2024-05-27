package svenhjol.charm.feature.suspicious_effect_improvements.suspicious_effects_last_longer;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.suspicious_effect_improvements.SuspiciousEffectImprovements;
import svenhjol.charm.feature.suspicious_effect_improvements.suspicious_effects_last_longer.common.Handlers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(description = "Suspicious effects from stews and flowers last longer.")
public final class SuspiciousEffectsLastLonger extends CommonFeature implements ChildFeature<SuspiciousEffectImprovements> {
    public final Handlers handlers;

    @Configurable(
        name = "Beneficial effect multiplier",
        description = "The duration of beneficial suspicious effects (such as strength) will be multiplied by this number."
    )
    private static int beneficialMultiplier = 4;

    @Configurable(
        name = "Detrimental effect multiplier",
        description = "The duration of detrimental suspicious effects (such as poison) will be multiplied by this number."
    )
    private static int detrimentalMultiplier = 2;

    public SuspiciousEffectsLastLonger(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public Class<SuspiciousEffectImprovements> typeForParent() {
        return SuspiciousEffectImprovements.class;
    }

    public int beneficialMultiplier() {
        return isEnabled() ? Mth.clamp(beneficialMultiplier, 1, 100) : 1;
    }

    public int detrimentalMultiplier() {
        return isEnabled() ? Mth.clamp(detrimentalMultiplier, 1, 100) : 1;
    }
}
