package svenhjol.charm.module;

import svenhjol.charm.client.ShulkerBoxTooltipsClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Shows the contents of a Shulker Box on hover-over.")
public class ShulkerBoxTooltips extends MesonModule {
    @Override
    public void clientInit() {
        new ShulkerBoxTooltipsClient(this);
    }
}
