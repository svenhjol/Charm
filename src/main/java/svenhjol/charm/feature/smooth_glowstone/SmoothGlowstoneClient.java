package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.feature.smooth_glowstone.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public class SmoothGlowstoneClient extends ClientFeature {
    public final Registers registers;
    public final SmoothGlowstone common;

    public SmoothGlowstoneClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(SmoothGlowstone.class);
        registers = new Registers(this);
    }
}
