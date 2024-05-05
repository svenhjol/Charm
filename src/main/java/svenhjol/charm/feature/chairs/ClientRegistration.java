package svenhjol.charm.feature.chairs;

import svenhjol.charm.foundation.feature.Register;

public final class ClientRegistration extends Register<ChairsClient> {
    public ClientRegistration(ChairsClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().entityRenderer(Chairs.entity, () -> ChairEntityRenderer::new);
    }
}
