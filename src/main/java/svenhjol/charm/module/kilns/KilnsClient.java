package svenhjol.charm.module.kilns;

import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class KilnsClient extends CharmClientModule {
    public KilnsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientHelper.registerScreenHandler(Kilns.SCREEN_HANDLER, KilnScreen::new);
    }
}
