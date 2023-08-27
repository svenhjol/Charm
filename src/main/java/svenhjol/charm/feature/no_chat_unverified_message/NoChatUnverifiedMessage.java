package svenhjol.charm.feature.no_chat_unverified_message;

import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(
    mod = CharmClient.MOD_ID,
    description = "Disables the 'Chat messages can't be verified' nag when the server does not enforce secure profile."
)
public class NoChatUnverifiedMessage extends CharmFeature { }
