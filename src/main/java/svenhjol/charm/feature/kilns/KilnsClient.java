package svenhjol.charm.feature.kilns;

import svenhjol.charm.feature.kilns.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class KilnsClient extends ClientFeature implements CommonResolver<Kilns> {
    public final Registers registers;

    public KilnsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Kilns> typeForCommon() {
        return Kilns.class;
    }
}
