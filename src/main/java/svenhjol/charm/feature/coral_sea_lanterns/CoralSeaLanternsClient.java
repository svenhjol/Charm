package svenhjol.charm.feature.coral_sea_lanterns;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.coral_sea_lanterns.client.Registers;

@Feature
public final class CoralSeaLanternsClient extends ClientFeature implements LinkedFeature<CoralSeaLanterns> {
    public final Registers registers;

    public CoralSeaLanternsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<CoralSeaLanterns> typeForLinked() {
        return CoralSeaLanterns.class;
    }
}
