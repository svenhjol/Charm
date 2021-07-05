package svenhjol.charm.module.kilns;

import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientRegistryHelper;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = Kilns.class)
public class KilnsClient extends CharmModule {
    @Override
    public void register() {
        ClientRegistryHelper.screenHandler(Kilns.SCREEN_HANDLER, KilnScreen::new);
    }
}
