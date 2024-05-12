package svenhjol.charm.feature.inventory_tidying.common;

import svenhjol.charm.feature.inventory_tidying.InventoryTidying;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<InventoryTidying> {
    public Registers(InventoryTidying feature) {
        super(feature);
        var registry = feature.registry();

        // Client senders
        registry.clientPacketSender(Networking.C2STidyInventory.TYPE, Networking.C2STidyInventory.CODEC);

        // Server receivers
        registry.packetReceiver(Networking.C2STidyInventory.TYPE,
            () -> feature().handlers::handleTidyInventory);
    }
}
