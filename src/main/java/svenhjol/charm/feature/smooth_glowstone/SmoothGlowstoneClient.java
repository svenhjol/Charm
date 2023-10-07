package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID, feature = SmoothGlowstone.class)
public class SmoothGlowstoneClient extends CharmonyFeature {
    @Override
    public void runWhenEnabled() {
        CharmClient.instance().registry()
            .itemTab(SmoothGlowstone.blockItem, CreativeModeTabs.BUILDING_BLOCKS, Items.AMETHYST_BLOCK);
    }
}
