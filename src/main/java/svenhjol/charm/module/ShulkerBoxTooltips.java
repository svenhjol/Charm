package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.ShulkerBoxTooltipsClient;

@Module(mod = Charm.MOD_ID, client = ShulkerBoxTooltipsClient.class, description = "Shows the contents of a Shulker Box on hover-over.")
public class ShulkerBoxTooltips extends CharmModule {
}
