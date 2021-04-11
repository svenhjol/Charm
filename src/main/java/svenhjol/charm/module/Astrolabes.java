package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.AstrolabesClient;
import svenhjol.charm.item.AstrolabeItem;

@Module(mod = Charm.MOD_ID, client = AstrolabesClient.class)
public class Astrolabes extends CharmModule {
    public static AstrolabeItem ASTROLABE;

    @Override
    public void register() {
        ASTROLABE = new AstrolabeItem(this);
    }
}
