package svenhjol.charm.feature.clear_item_frames;

import svenhjol.charm.foundation.feature.Register;

public final class ClientRegistration extends Register<ClearItemFramesClient> {
    public ClientRegistration(ClearItemFramesClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        ClearItemFramesClient.particle = feature.registry().particle(ClearItemFrames.particleType,
            () -> ApplyAmethystClientParticle::new);
    }
}
