package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class SmoothGlowstoneClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return SmoothGlowstone.class;
    }

    @Override
    public void runWhenEnabled() {
        mod().registry().itemTab(SmoothGlowstone.blockItem, CreativeModeTabs.BUILDING_BLOCKS, Items.AMETHYST_BLOCK);
    }
}
