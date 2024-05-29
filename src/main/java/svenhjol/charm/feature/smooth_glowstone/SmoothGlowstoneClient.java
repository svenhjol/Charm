package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.feature.smooth_glowstone.client.Registers;

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
