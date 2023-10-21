package svenhjol.charm.feature.no_spyglass_scope;

import svenhjol.charm.Charm;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.client.ClientFeature;

public class NoSpyglassScope extends ClientFeature {
    @Override
    public String description() {
        return "Removes the dark outer overlay when zooming in with the spyglass, giving you the full view.";
    }

    public static boolean shouldRemoveHud() {
        return Mods.client(Charm.ID).loader().isEnabled(NoSpyglassScope.class);
    }
}
