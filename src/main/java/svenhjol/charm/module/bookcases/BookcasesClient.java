package svenhjol.charm.module.bookcases;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = Bookcases.class)
public class BookcasesClient extends CharmClientModule {
    @Override
    public void register() {
        ScreenRegistry.register(Bookcases.SCREEN_HANDLER, BookcaseScreen::new);
    }
}
