package svenhjol.charm.feature.chairs.client;

import svenhjol.charm.feature.chairs.Chairs;
import svenhjol.charm.feature.chairs.ChairsClient;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.feature.Register;

public final class Registers extends Register<ChairsClient> {
    private static final Chairs FEATURE = Resolve.feature(Chairs.class);

    public Registers(ChairsClient feature) {
        super(feature);

        feature.registry().entityRenderer(FEATURE.registers().entity,
            () -> EntityRenderer::new);
    }
}
