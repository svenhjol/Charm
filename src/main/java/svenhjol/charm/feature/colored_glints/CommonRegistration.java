package svenhjol.charm.feature.colored_glints;

import svenhjol.charm.foundation.Registration;

public final class CommonRegistration extends Registration<ColoredGlints> {
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
