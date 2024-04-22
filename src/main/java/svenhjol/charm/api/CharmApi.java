package svenhjol.charm.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import svenhjol.charm.foundation.helper.ApiHelper;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue", "unused"})
public class CharmApi {
    private static final Logger LOGGER = LogManager.getLogger("CharmApi");

    public static void registerProvider(Object provider) {
        ApiHelper.registerProvider(provider);
    }
}
