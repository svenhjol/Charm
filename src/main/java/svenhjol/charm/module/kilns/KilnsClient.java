package svenhjol.charm.module.kilns;

import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class KilnsClient extends CharmClientModule {
    public KilnsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        RegistryHelper.clientScreenHandler(Kilns.SCREEN_HANDLER, KilnScreen::new);
    }
}
