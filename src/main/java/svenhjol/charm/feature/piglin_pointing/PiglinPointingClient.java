package svenhjol.charm.feature.piglin_pointing;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.feature.piglin_pointing.client.Handlers;

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
