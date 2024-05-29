package svenhjol.charm.feature.chairs;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.chairs.client.Registers;

@Feature
public final class ChairsClient extends ClientFeature implements LinkedFeature<Chairs> {
    public final Registers registers;

    public ChairsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Chairs> typeForLinked() {
        return Chairs.class;
    }
}
