package svenhjol.charm.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public final class CharmApi {
    private static final Logger LOGGER = LogManager.getLogger("CharmApi");
    private static final String API_HELPER = "svenhjol.charm.charmony.Api";

    public static void registerProvider(Object provider) {
        try {
            var clazz = Class.forName(API_HELPER);
            var method = clazz.getMethod("registerProvider", Object.class);
            method.invoke(null, provider);
        } catch (Exception e) {
            // Package probably not installed, just skip.
            LOGGER.debug("API call failed.");
        }
    }
}
