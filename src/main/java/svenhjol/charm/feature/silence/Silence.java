package svenhjol.charm.feature.silence;

import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(enabledByDefault = false, description = """
    Disables some nag messages and telemetry.
    Some data removal may be considered controversial so this feature is disabled by defualt.""")
public final class Silence extends CommonFeature {
    public Silence(CommonLoader loader) {
        super(loader);
    }

    @Configurable(name = "Disable chat message verification dialog", description = """
        If true, disables the 'Chat messages can't be verified' dialog message when the server does not enforce secure profile.
        This only applies if you set 'enforce-secure-profile' to true in server.properties""")
    public static boolean disableChatMessageVerification = true;

    @Configurable(name = "Disable experimental screen dialog", description = """
        If true, disables the 'Experimental' warning dialog that appears when opening a world with experimental features enabled.""")
    public static boolean disableExperimental = true;

    @Configurable(name = "Disable telemetry", description = """
        If true, prevents the client telemetry manager from ever sending any messages back to the mothership.
        Telemetry includes your game session, game version, operating system and launcher.""")
    public static boolean disableTelemetry = true;

    @Configurable(name = "Disable development mode connections", description = """
        If true, disables realms and other API connections when running in the development environment.
        Setting this to true doesn't do anything if you are playing in a launcher.""")
    public static boolean disableDevEnvironmentConnections = true;

    public boolean disableExperimental() {
        return disableExperimental;
    }
}
