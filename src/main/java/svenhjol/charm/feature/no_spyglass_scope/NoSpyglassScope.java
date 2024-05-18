package svenhjol.charm.feature.no_spyglass_scope;

import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "Removes the dark outer overlay when zooming in with the spyglass.")
public final class NoSpyglassScope extends ClientFeature {
    public NoSpyglassScope(ClientLoader loader) {
        super(loader);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
