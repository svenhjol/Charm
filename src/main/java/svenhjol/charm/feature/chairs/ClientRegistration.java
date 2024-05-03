package svenhjol.charm.feature.chairs;

import svenhjol.charm.foundation.Registration;

public final class ClientRegistration extends Registration<ChairsClient> {
    public ClientRegistration(ChairsClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().entityRenderer(Chairs.entity, () -> ChairEntityRenderer::new);
    }
}
