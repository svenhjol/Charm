package svenhjol.charm.feature.silence.common;

import svenhjol.charm.feature.silence.Silence;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.ConfigHelper;

public final class Handlers extends FeatureHolder<Silence> {
    public Handlers(Silence feature) {
        super(feature);
    }

    /**
     * Special static method for mixins to access.
     */
    public static boolean disableDevEnvironmentConnections() {
        return Silence.disableDevEnvironmentConnections
            && ConfigHelper.isDevEnvironment();
    }
}
