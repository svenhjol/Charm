package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.Registration;

public class ClientRegistration extends Registration<SmoothGlowstoneClient> {
    public ClientRegistration(SmoothGlowstoneClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().itemTab(SmoothGlowstone.blockItem, CreativeModeTabs.BUILDING_BLOCKS, Items.AMETHYST_BLOCK);
    }
}
