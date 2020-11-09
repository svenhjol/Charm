package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

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
