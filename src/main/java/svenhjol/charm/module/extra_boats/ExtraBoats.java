package svenhjol.charm.module.extra_boats;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, priority = 5, alwaysEnabled = true, description = "Allows Charm to register extra boat types.")
public class ExtraBoats extends CharmModule {
}
