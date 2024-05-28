package svenhjol.charm.feature.casks.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.casks.CasksClient;
import svenhjol.charm.feature.casks.common.Networking;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CasksClient> {
    public Registers(CasksClient feature) {
        super(feature);

        // Server to client receivers
        feature.registry().packetReceiver(Networking.S2CAddedToCask.TYPE,
            () -> feature().handlers::addedToCaskReceived);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        registry.itemTab(
            feature().common().registers.block,
            CreativeModeTabs.FUNCTIONAL_BLOCKS,
            Items.JUKEBOX);
    }
}
