package svenhjol.charm.feature.clear_item_frames;

import svenhjol.charm.foundation.Networking;

public class ClientNetworking extends Networking<ClearItemFramesClient> {
    public ClientNetworking(ClearItemFramesClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().packetReceiver(CommonNetworking.AddAmethyst.TYPE,
            ClearItemFramesClient::handleAddToItemFrame);
        feature.registry().packetReceiver(CommonNetworking.RemoveAmethyst.TYPE,
            ClearItemFramesClient::handleRemoveFromItemFrame);
    }
}
