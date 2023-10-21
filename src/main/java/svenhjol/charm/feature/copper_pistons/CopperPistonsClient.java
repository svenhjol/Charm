package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class CopperPistonsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return CopperPistons.class;
    }

    @Override
    public void runWhenEnabled() {
        var registry = mod().registry();

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
