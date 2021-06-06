package svenhjol.charm.module.bookcases;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.bookcases.BookcaseScreen;
import svenhjol.charm.module.bookcases.Bookcases;

public class BookcasesClient extends CharmClientModule {
    public BookcasesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ScreenRegistry.register(Bookcases.SCREEN_HANDLER, BookcaseScreen::new);
    }
}
