package svenhjol.charm.feature.coral_squids;

import svenhjol.charm.feature.coral_squids.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class CoralSquidsClient extends ClientFeature implements CommonResolver<CoralSquids> {
    public final Registers registers;

    public CoralSquidsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<CoralSquids> typeForCommon() {
        return CoralSquids.class;
    }
}
