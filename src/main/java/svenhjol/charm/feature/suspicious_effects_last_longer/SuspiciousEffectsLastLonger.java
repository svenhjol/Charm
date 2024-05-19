package svenhjol.charm.feature.suspicious_effects_last_longer;

import svenhjol.charm.feature.suspicious_effects_last_longer.common.Handlers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Suspicious effects from stews and flowers last longer.")
public class SuspiciousEffectsLastLonger extends CommonFeature {
    public final Handlers handlers;

    @Configurable(
        name = "Beneficial effect multiplier",
        description = "The duration of beneficial suspicious effects (such as strength) will be multiplied by this number."
    )
    public static int beneficialMultiplier = 4;

    @Configurable(
        name = "Detrimental effect multiplier",
        description = "The duration of detrimental suspicious effects (such as poison) will be multiplied by this number."
    )
    public static int detrimentalMultiplier = 2;

    public SuspiciousEffectsLastLonger(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }
}
