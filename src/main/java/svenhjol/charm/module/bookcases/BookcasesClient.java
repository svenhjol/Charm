package svenhjol.charm.module.bookcases;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class BookcasesClient extends CharmClientModule {
    public BookcasesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ScreenRegistry.register(Bookcases.SCREEN_HANDLER, BookcaseScreen::new);
    }
}
