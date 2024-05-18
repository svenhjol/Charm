package svenhjol.charm.feature.spyglass_scope_hiding;

import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "Removes the border when zooming in with the spyglass.")
public final class SpyglassScopeHiding extends ClientFeature {
    public SpyglassScopeHiding(ClientLoader loader) {
        super(loader);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
