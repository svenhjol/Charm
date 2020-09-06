package svenhjol.charm.module;

import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(alwaysEnabled = true, description = "Hacks to change behavior or make things work.")
public class Hacks extends MesonModule {
    @Config(
        name = "Charm hack: Enchanting Table",
        description = "If true, changes the code that the vanilla Enchanting Table uses to detect enchanting blocks around it."
    )
    public static boolean hackEnchantingTable = true;
}
