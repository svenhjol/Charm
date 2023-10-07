package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID, feature = CopperPistons.class)
public class CopperPistonsClient extends CharmonyFeature {
    @Override
    public void runWhenEnabled() {
        var registry = CharmClient.instance().registry();

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
