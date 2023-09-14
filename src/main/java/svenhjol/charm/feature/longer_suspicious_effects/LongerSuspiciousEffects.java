package svenhjol.charm.feature.longer_suspicious_effects;

import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "Suspicious effects with positive benefits last longer.")
public class LongerSuspiciousEffects extends CharmFeature {
    @Configurable(
        name = "Effect multiplier",
        description = "Beneficial suspicious effect duration will be multiplied by this number."
    )
    public static int effectMultiplier = 5;
}
