package svenhjol.charm.feature.no_chat_unverified_message;

import svenhjol.charmony.client.ClientFeature;

public class NoChatUnverifiedMessage extends ClientFeature {
    @Override
    public String description() {
        return "Disables the 'Chat messages can't be verified' nag when the server does not enforce secure profile.";
    }
}
