package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.smooth_glowstone.common.Providers;
import svenhjol.charm.feature.smooth_glowstone.common.Registers;

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
