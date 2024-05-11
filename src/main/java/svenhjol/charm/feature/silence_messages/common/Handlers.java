package svenhjol.charm.feature.silence_messages.common;

import svenhjol.charm.feature.silence_messages.SilenceMessages;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.ConfigHelper;

public final class Handlers extends FeatureHolder<SilenceMessages> {
    public Handlers(SilenceMessages feature) {
        super(feature);
    }

    /**
     * Special static method for mixins to access.
     */
    public static boolean disableDevEnvironmentConnections() {
        return SilenceMessages.disableDevEnvironmentConnections
            && ConfigHelper.isDevEnvironment();
    }
}
