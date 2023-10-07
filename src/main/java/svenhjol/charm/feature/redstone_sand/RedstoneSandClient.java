package svenhjol.charm.feature.redstone_sand;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID, feature = RedstoneSand.class)
public class RedstoneSandClient extends CharmonyFeature {
    @Override
    public void runWhenEnabled() {
        CharmClient.instance().registry().itemTab(
            RedstoneSand.block,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.REDSTONE_BLOCK
        );
    }
}
