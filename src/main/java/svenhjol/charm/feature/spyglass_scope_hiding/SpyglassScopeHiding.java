package svenhjol.charm.feature.spyglass_scope_hiding;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;

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
