package svenhjol.charm.module.remove_spyglass_scope;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "Removes the dark outer overlay when zooming in with the spyglass, giving you the full view.")
public class RemoveSpyglassScope extends CharmModule {
    public static boolean isEnabled;

    @Override
    public void register() {
        isEnabled = this.enabled;
    }

    public static boolean shouldRemoveHud() {
        return isEnabled;
    }
}
