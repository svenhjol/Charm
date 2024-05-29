package svenhjol.charm.feature.kilns;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.kilns.client.Registers;

@Feature
public final class KilnsClient extends ClientFeature implements LinkedFeature<Kilns> {
    public final Registers registers;

    public KilnsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Kilns> typeForLinked() {
        return Kilns.class;
    }
}
