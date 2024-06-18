package svenhjol.charm.feature.copper_pistons.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.copper_pistons.CopperPistonsClient;

public final class Registers extends RegisterHolder<CopperPistonsClient> {
    public Registers(CopperPistonsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        registry.itemTab(
            feature().linked().registers.stickyCopperPistonBlockItem.get(),
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.STICKY_PISTON
        );
        registry.itemTab(
            feature().linked().registers.copperPistonBlockItem.get(),
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.STICKY_PISTON
        );
    }
}
