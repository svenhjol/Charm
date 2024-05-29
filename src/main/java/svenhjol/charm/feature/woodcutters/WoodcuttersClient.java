package svenhjol.charm.feature.woodcutters;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.woodcutters.client.Registers;

@Feature
public final class WoodcuttersClient extends ClientFeature implements LinkedFeature<Woodcutters> {
    public final Registers registers;

    public WoodcuttersClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Woodcutters> typeForLinked() {
        return Woodcutters.class;
    }
}
