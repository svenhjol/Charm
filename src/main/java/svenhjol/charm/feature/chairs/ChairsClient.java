package svenhjol.charm.feature.chairs;

import svenhjol.charm.feature.chairs.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class ChairsClient extends ClientFeature implements CommonResolver<Chairs> {
    public final Registers registers;

    public ChairsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Chairs> typeForCommon() {
        return Chairs.class;
    }
}
