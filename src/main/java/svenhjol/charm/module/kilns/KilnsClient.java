package svenhjol.charm.module.kilns;

import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = Kilns.class)
public class KilnsClient extends ClientModule {
    @Override
    public void register() {
        ClientHelper.registerScreenHandler(Kilns.SCREEN_HANDLER, KilnScreen::new);
    }
}
