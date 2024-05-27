package svenhjol.charm.feature.piglin_pointing;

import svenhjol.charm.feature.piglin_pointing.client.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class PiglinPointingClient extends ClientFeature implements CommonResolver<PiglinPointing> {
    public final Handlers handlers;

    public PiglinPointingClient(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public Class<PiglinPointing> typeForCommon() {
        return PiglinPointing.class;
    }
}
