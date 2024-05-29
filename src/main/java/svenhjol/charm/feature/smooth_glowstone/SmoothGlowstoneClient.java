package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.smooth_glowstone.client.Registers;

@Feature
public final class SmoothGlowstoneClient extends ClientFeature implements LinkedFeature<SmoothGlowstone> {
    public final Registers registers;

    public SmoothGlowstoneClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<SmoothGlowstone> typeForLinked() {
        return SmoothGlowstone.class;
    }
}
