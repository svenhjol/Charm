package svenhjol.charm.feature.grindstone_disenchanting;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.grindstone_disenchanting.client.Handlers;

@Feature
public final class GrindstoneDisenchantingClient extends ClientFeature implements LinkedFeature<GrindstoneDisenchanting> {
    public final Handlers handlers;

    public GrindstoneDisenchantingClient(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public Class<GrindstoneDisenchanting> typeForLinked() {
        return GrindstoneDisenchanting.class;
    }
}
