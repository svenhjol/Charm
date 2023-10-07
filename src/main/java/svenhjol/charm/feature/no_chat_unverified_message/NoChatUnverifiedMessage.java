package svenhjol.charm.feature.no_chat_unverified_message;

import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(
    mod = CharmClient.MOD_ID,
    description = "Disables the 'Chat messages can't be verified' nag when the server does not enforce secure profile."
)
public class NoChatUnverifiedMessage extends CharmonyFeature { }
