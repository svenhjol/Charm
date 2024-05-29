package svenhjol.charm.feature.chairs.client;

import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.chairs.ChairsClient;

public final class Registers extends RegisterHolder<ChairsClient> {
    public Registers(ChairsClient feature) {
        super(feature);

        feature.registry().entityRenderer(feature.common().registers.entity,
            () -> EntityRenderer::new);
    }
}
