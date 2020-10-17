package svenhjol.charm.module.core;

import svenhjol.charm.Charm;
import svenhjol.charm.client.ShulkerBoxTooltipsClient;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Shows the contents of a Shulker Box on hover-over.")
public class ShulkerBoxTooltips extends CharmModule {
    @Override
    public void clientInit() {
        new ShulkerBoxTooltipsClient(this);
    }
}
