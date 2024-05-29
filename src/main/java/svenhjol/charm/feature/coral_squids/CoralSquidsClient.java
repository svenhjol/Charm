package svenhjol.charm.feature.coral_squids;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.coral_squids.client.Registers;

@Feature
public final class CoralSquidsClient extends ClientFeature implements LinkedFeature<CoralSquids> {
    public final Registers registers;

    public CoralSquidsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<CoralSquids> typeForLinked() {
        return CoralSquids.class;
    }
}
