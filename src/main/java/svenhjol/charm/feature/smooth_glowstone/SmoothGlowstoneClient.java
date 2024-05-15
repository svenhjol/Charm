package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.feature.smooth_glowstone.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class SmoothGlowstoneClient extends ClientFeature implements CommonResolver<SmoothGlowstone> {
    public final Registers registers;

    public SmoothGlowstoneClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<SmoothGlowstone> typeForCommon() {
        return SmoothGlowstone.class;
    }
}
