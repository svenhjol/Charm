package svenhjol.charm.feature.clear_item_frames;

import svenhjol.charm.foundation.Networking;

public class ClientNetworking extends Networking<ClearItemFramesClient> {
    public ClientNetworking(ClearItemFramesClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().packet(ServerNetworking.AddAmethyst.TYPE, ServerNetworking.AddAmethyst.CODEC,
            ClearItemFramesClient::handleAddToItemFrame);
        feature.registry().packet(ServerNetworking.RemoveAmethyst.TYPE, ServerNetworking.RemoveAmethyst.CODEC,
            ClearItemFramesClient::handleRemoveFromItemFrame);
    }
}
