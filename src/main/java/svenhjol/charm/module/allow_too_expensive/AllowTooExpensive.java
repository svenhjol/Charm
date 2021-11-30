package svenhjol.charm.module.allow_too_expensive;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Allows anvil repair of items with repair cost 39 or more.")
public class AllowTooExpensive extends CharmModule {
}
