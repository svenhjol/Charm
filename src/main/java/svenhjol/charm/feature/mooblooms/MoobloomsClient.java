package svenhjol.charm.feature.mooblooms;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.feature.mooblooms.client.Registers;

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
