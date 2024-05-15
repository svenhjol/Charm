package svenhjol.charm.feature.woodcutters;

import svenhjol.charm.feature.woodcutters.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

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
