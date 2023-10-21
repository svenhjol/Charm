package svenhjol.charm.feature.redstone_sand;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class RedstoneSandClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return RedstoneSand.class;
    }

    @Override
    public void runWhenEnabled() {
        mod().registry().itemTab(
            RedstoneSand.block,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.REDSTONE_BLOCK
        );
    }
}
