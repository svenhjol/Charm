package svenhjol.charm.feature.item_tidying.common;

import svenhjol.charm.feature.item_tidying.ItemTidying;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ItemTidying> {
    public Registers(ItemTidying feature) {
        super(feature);
        var registry = feature.registry();

        // Server receivers
        registry.serverPacketReceiver(new Networking.C2STidyInventory(),
            () -> feature().handlers::handleTidyInventory);
    }
}
