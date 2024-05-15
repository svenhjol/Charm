package svenhjol.charm.feature.silence;

import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Disables some nag messages and telemetry.")
public final class Silence extends CommonFeature {
    public Silence(CommonLoader loader) {
        super(loader);
    }

    @Configurable(name = "Disable chat message nag", description = """
        If true, disables the 'Chat messages can't be verified' nag message when the server does not enforce secure profile.
        This only applies if you set 'enforce-secure-profile' to true in server.properties""")
    public static boolean disableChatMessageNag = true;

    @Configurable(name = "Disable telemetry", description = """
        If true, prevents the client telemetry manager from ever sending any messages back to the mothership.
        Telemetry includes your game session, game version, operating system and launcher.""")
    public static boolean disableTelemetry = false;

    @Configurable(name = "Disable development mode connections", description = """
        If true, disables realms and other API connections when running in the development environment.
        Setting this to true doesn't do anything if you are playing in a launcher.""")
    public static boolean disableDevEnvironmentConnections = true;
}
