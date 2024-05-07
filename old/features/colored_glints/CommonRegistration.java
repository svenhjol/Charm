package svenhjol.charm.feature.colored_glints;

import svenhjol.charm.foundation.feature.Register;

public final class CommonRegistration extends Register<ColoredGlints> {
    public CommonRegistration(ColoredGlints feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        ColoredGlints.data = feature.registry().dataComponent("charm_colored_glint",
            () -> builder -> builder
                .persistent(ColoredGlintData.CODEC)
                .networkSynchronized(ColoredGlintData.STREAM_CODEC));
    }
}
