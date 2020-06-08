package svenhjol.charm.base.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.CORE, hasSubscriptions = true,
    description = "Internal debugging tests for Charm.")
public class Debug extends MesonModule {

    @Config(name = "Show debug messages", description = "If true, shows debug messages in the Minecraft log.\n" +
        "This applies to all Meson-based mods, e.g. Charm, Strange, Covalent.")
    public static boolean showDebugMessages = false;
}
