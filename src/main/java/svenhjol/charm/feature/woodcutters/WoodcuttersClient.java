package svenhjol.charm.feature.woodcutters;

import svenhjol.charm.feature.woodcutters.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;

@Feature
public final class WoodcuttersClient extends ClientFeature implements CommonResolver<Woodcutters> {
    public final Registers registers;

    public WoodcuttersClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Woodcutters> typeForCommon() {
        return Woodcutters.class;
    }
}
