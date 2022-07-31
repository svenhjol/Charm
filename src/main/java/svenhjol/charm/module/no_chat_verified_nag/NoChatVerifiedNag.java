package svenhjol.charm.module.no_chat_verified_nag;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Disables the 'Chat messages can't be verified' nag when the server does not enforce secure profile.\n" +
    "This is a controversial module and so is disabled by default.", enabledByDefault = false)
public class NoChatVerifiedNag extends CharmModule {

}
