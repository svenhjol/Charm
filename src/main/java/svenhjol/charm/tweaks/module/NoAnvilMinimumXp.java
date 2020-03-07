package svenhjol.charm.tweaks.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS,
    description = "Enabled anvil actions to be performed without an XP cost." +
        "This only applies to Charm actions and affects no vanilla functionality.")
public class NoAnvilMinimumXp extends MesonModule {
}
