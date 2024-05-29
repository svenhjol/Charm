package svenhjol.charm.feature.core.common;

import svenhjol.charm.Charm;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.helper.ConfigHelper;

public final class CharmDebugChecks {
    private static boolean hasCheckedDebugConfig = false;
    private static boolean hasCheckedCompatConfig = false;
    private static boolean cachedDebugValue = false;
    private static boolean cachedCompatValue = false;

    /**
     * Helper method to check if debug mode is enabled in Charm's config file.
     * In Fabric, running Charm in the dev environment will always return true.
     */
    public static boolean isDebugEnabled() {
        if (ConfigHelper.isDevEnvironment()) return true;

        if (!hasCheckedDebugConfig) {
            var toml = ConfigHelper.read(ConfigHelper.filename(Charm.ID, Side.COMMON));
            var key = "Core.\"" + ConfigHelper.DEBUG_MODE + "\"";
            if (toml.contains(key)) {
                cachedDebugValue = toml.getBoolean(key);
            }

            hasCheckedDebugConfig = true;
        }

        return cachedDebugValue;
    }

    /**
     * Helper method to check if compat mode is enabled in Charm's config file.
     */
    public static boolean isCompatEnabled() {
        if (!hasCheckedCompatConfig) {
            var toml = ConfigHelper.read(ConfigHelper.filename(Charm.ID, Side.COMMON));
            var key = "Core.\"" + ConfigHelper.COMPAT_MODE + "\"";
            if (toml.contains(key)) {
                cachedCompatValue = toml.getBoolean(key);
            }

            hasCheckedCompatConfig = true;
        }

        return cachedCompatValue;
    }
}
