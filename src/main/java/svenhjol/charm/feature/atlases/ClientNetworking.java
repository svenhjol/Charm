package svenhjol.charm.feature.atlases;

import svenhjol.charm.feature.atlases.CommonNetworking.*;
import svenhjol.charm.foundation.feature.Network;

public final class ClientNetworking extends Network<AtlasesClient> {
    public ClientNetworking(AtlasesClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        // Client receivers
        registry.packetReceiver(S2CSwappedAtlasSlot.TYPE,
            AtlasesClient::handleSwappedSlot);
        registry.packetReceiver(S2CUpdateInventory.TYPE,
            AtlasesClient::handleUpdateInventory);
    }
}
