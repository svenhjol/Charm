package svenhjol.charm.feature.copper_pistons;

import svenhjol.charm.feature.copper_pistons.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class CopperPistonsClient extends ClientFeature implements CommonResolver<CopperPistons> {
    public final Registers registers;

    public CopperPistonsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<CopperPistons> typeForCommon() {
        return CopperPistons.class;
    }
}
