package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.feature.smooth_glowstone.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Smooth glowstone.")
public class SmoothGlowstone extends CommonFeature {
    public final Registers registers;

    public SmoothGlowstone(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
