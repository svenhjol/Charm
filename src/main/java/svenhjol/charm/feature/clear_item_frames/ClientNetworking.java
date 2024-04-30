package svenhjol.charm.feature.clear_item_frames;

import svenhjol.charm.foundation.Networking;

public class ClientNetworking extends Networking<ClearItemFramesClient> {
    public ClientNetworking(ClearItemFramesClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();
        registry.packetReceiver(CommonNetworking.AddAmethyst.TYPE,
            ClearItemFramesClient::handleAddToItemFrame);
        registry.packetReceiver(CommonNetworking.RemoveAmethyst.TYPE,
            ClearItemFramesClient::handleRemoveFromItemFrame);
    }
}
