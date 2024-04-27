package svenhjol.charm.feature.clear_item_frames;

import svenhjol.charm.foundation.Registration;

public class ClientRegistration extends Registration<ClearItemFramesClient> {
    public ClientRegistration(ClearItemFramesClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        ClearItemFramesClient.particle = feature.registry().particle(ClearItemFrames.particleType,
            () -> ApplyAmethystClientParticle::new);
    }
}
