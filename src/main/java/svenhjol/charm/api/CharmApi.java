package svenhjol.charm.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import svenhjol.charm.foundation.Api;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue", "unused"})
public final class CharmApi {
    private static final Logger LOGGER = LogManager.getLogger("CharmApi");

    public static void registerProvider(Object provider) {
        Api.registerProvider(provider);
    }
}
