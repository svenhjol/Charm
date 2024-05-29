package svenhjol.charm.feature.crafting_from_inventory.common;

import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventory;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CraftingFromInventory> {
    public Registers(CraftingFromInventory feature) {
        super(feature);
        var registry = feature.registry();

        // Client packet senders
        registry.clientPacketSender(Networking.C2SOpenPortableCrafting.TYPE,
            Networking.C2SOpenPortableCrafting.CODEC);

        // Server packet receivers
        registry.packetReceiver(Networking.C2SOpenPortableCrafting.TYPE,
            () -> feature().handlers::openPortableCraftingReceived);
    }
}
