package svenhjol.charm.feature.silence_messages;

import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Disables some nag messages/notifications.")
public class SilenceMessages extends CommonFeature {
    public SilenceMessages(CommonLoader loader) {
        super(loader);
    }

    @Configurable(name = "Disable chat message nag", description = """
        If true, disables the 'Chat messages can't be verified' nag when the server does not enforce secure profile.""")
    public static boolean disableChatMessageNag = true;

    @Configurable(name = "Disable development mode connections", description = """
        If true, disables realms and other API connections when running in the development environment.
        Setting this to true doesn't do anything if you are playing in a launcher.""")
    public static boolean disableDevEnvironmentConnections = true;
}
