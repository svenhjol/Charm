package svenhjol.charm.feature.endermite_powder;

import svenhjol.charm.feature.endermite_powder.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class EndermitePowderClient extends ClientFeature implements CommonResolver<EndermitePowder> {
    public final Registers registers;

    public EndermitePowderClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<EndermitePowder> typeForCommon() {
        return EndermitePowder.class;
    }
}
