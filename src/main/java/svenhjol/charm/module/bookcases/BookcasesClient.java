package svenhjol.charm.module.bookcases;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = Bookcases.class)
public class BookcasesClient extends ClientModule {
    @Override
    public void register() {
        ScreenRegistry.register(Bookcases.SCREEN_HANDLER, BookcaseScreen::new);
    }
}
