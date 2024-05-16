package svenhjol.charm.feature.coral_sea_lanterns;

import svenhjol.charm.feature.coral_sea_lanterns.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class CoralSeaLanternsClient extends ClientFeature implements CommonResolver<CoralSeaLanterns> {
    public final Registers registers;

    public CoralSeaLanternsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<CoralSeaLanterns> typeForCommon() {
        return CoralSeaLanterns.class;
    }
}
