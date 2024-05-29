package svenhjol.charm.feature.endermite_powder;

import svenhjol.charm.feature.endermite_powder.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;

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
