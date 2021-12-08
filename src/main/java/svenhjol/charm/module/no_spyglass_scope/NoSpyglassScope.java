package svenhjol.charm.module.no_spyglass_scope;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Removes the dark outer overlay when zooming in with the spyglass, giving you the full view.")
public class NoSpyglassScope extends CharmModule {
    public static boolean shouldRemoveHud() {
        return Charm.LOADER.isEnabled(NoSpyglassScope.class);
    }
}
