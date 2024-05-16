package svenhjol.charm.feature.mooblooms;

import svenhjol.charm.feature.mooblooms.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class MoobloomsClient extends ClientFeature implements CommonResolver<Mooblooms> {
    public final Registers registers;

    public MoobloomsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Mooblooms> typeForCommon() {
        return Mooblooms.class;
    }
}
