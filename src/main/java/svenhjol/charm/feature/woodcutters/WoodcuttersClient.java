package svenhjol.charm.feature.woodcutters;

import svenhjol.charm.feature.woodcutters.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(canBeDisabled = false)
public class WoodcuttersClient extends ClientFeature {
    public final Woodcutters common;
    public final Registers registers;

    public WoodcuttersClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(Woodcutters.class);
        registers = new Registers(this);
    }
}
