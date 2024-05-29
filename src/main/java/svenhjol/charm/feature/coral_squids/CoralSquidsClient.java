package svenhjol.charm.feature.coral_squids;

import svenhjol.charm.feature.coral_squids.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;

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
