package svenhjol.charm.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import svenhjol.charm.Charm;
import svenhjol.charm.charmony.client.screen.CharmSettingsScreen;

public class ModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new CharmSettingsScreen(Charm.ID, parent);
    }
}
