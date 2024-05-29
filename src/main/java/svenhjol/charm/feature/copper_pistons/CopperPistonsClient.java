package svenhjol.charm.feature.copper_pistons;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.copper_pistons.client.Registers;

@Feature
public final class CopperPistonsClient extends ClientFeature implements LinkedFeature<CopperPistons> {
    public final Registers registers;

    public CopperPistonsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<CopperPistons> typeForLinked() {
        return CopperPistons.class;
    }
}
