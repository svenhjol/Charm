package svenhjol.charm.feature.longer_suspicious_effects;

import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;

public class LongerSuspiciousEffects extends CommonFeature {
    @Configurable(
        name = "Effect multiplier",
        description = "Beneficial suspicious effect duration will be multiplied by this number."
    )
    public static int effectMultiplier = 4;

    @Override
    public String description() {
        return "Beneficial suspicious stew effects last longer.";
    }
}
