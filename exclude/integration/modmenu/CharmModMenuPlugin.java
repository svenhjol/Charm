package svenhjol.charm.integration.modmenu;

import svenhjol.charm.Charm;
import svenhjol.charm.loader.CharmModule;

import java.util.List;

public class CharmModMenuPlugin extends BaseModMenuPlugin<CharmModule> {
    @Override
    public String getModId() {
        return Charm.MOD_ID;
    }

    @Override
    public List<CharmModule> getModules() {
        return Charm.LOADER.getModules();
    }
}
