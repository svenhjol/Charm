package svenhjol.charm.feature.chairs;

import svenhjol.charm.foundation.Register;

public class ClientRegister extends Register<ChairsClient> {
    public ClientRegister(ChairsClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().entityRenderer(Chairs.entity, () -> ChairEntityRenderer::new);
    }
}
