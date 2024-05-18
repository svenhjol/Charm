package svenhjol.charm.feature.grindstone_disenchanting;

import svenhjol.charm.feature.grindstone_disenchanting.client.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class GrindstoneDisenchantingClient extends ClientFeature implements CommonResolver<GrindstoneDisenchanting> {
    public final Handlers handlers;

    public GrindstoneDisenchantingClient(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public Class<GrindstoneDisenchanting> typeForCommon() {
        return GrindstoneDisenchanting.class;
    }
}
