package svenhjol.charm.module.bookcases;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = Bookcases.class)
public class BookcasesClient extends CharmModule {
    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Charm.MOD_ID, "textures/gui/bookcase.png");
    @Override

    public void register() {
        MenuScreens.register(Bookcases.MENU, BookcaseScreen::new);
    }
}
