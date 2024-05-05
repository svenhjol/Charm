package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.feature.Register;

public final class ClientRegistration extends Register<CopperPistonsClient> {
    public ClientRegistration(CopperPistonsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        var registry = feature.registry();

        registry.itemTab(
            CopperPistons.stickyCopperPistonBlockItem,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.STICKY_PISTON
        );
        registry.itemTab(
            CopperPistons.copperPistonBlockItem,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.STICKY_PISTON
        );
    }
}
