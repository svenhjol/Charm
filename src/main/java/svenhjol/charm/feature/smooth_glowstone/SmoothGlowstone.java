package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.feature.smooth_glowstone.common.Providers;
import svenhjol.charm.feature.smooth_glowstone.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Smooth glowstone.")
public final class SmoothGlowstone extends CommonFeature {
    public final Registers registers;
    public final Providers providers;

    public SmoothGlowstone(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
    }
}
