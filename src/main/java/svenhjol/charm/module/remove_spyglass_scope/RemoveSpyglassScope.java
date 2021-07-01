package svenhjol.charm.module.remove_spyglass_scope;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Removes the dark outer overlay when zooming in with the spyglass, giving you the full view.")
public class RemoveSpyglassScope extends svenhjol.charm.loader.CommonModule {
    public static boolean isEnabled;

    @Override
    public void register() {
        isEnabled = this.isEnabled();
    }

    public static boolean shouldRemoveHud() {
        return isEnabled;
    }
}
