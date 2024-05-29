package svenhjol.charm.feature.coral_sea_lanterns;

import svenhjol.charm.feature.coral_sea_lanterns.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;

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
