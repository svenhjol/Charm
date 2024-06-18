package svenhjol.charm.feature.crafting_from_inventory.common;

import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventory;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CraftingFromInventory> {
    public Registers(CraftingFromInventory feature) {
        super(feature);
        var registry = feature.registry();

        // Server packet receivers
        registry.serverPacketReceiver(new Networking.C2SOpenPortableCrafting(),
            () -> feature().handlers::openPortableCraftingReceived);
    }
}
