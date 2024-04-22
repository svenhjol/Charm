package svenhjol.charm;

import svenhjol.charm.feature.colored_glints.ColoredGlintsClient;
import svenhjol.charm.foundation.client.ClientFeature;

import java.util.List;

public class CharmClient {
    public static List<Class<? extends ClientFeature>> features() {
        return List.of(
            ColoredGlintsClient.class
        );
    }
}
