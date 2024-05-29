package svenhjol.charm.feature.item_tidying.common;

import svenhjol.charm.feature.item_tidying.ItemTidying;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ItemTidying> {
    public Registers(ItemTidying feature) {
        super(feature);
        var registry = feature.registry();

        // Client senders
        registry.clientPacketSender(Networking.C2STidyInventory.TYPE, Networking.C2STidyInventory.CODEC);

        // Server receivers
        registry.packetReceiver(Networking.C2STidyInventory.TYPE,
            () -> feature().handlers::handleTidyInventory);
    }
}
