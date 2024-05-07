package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.feature.Register;

public final class ClientRegistration extends Register<SmoothGlowstoneClient> {
    public ClientRegistration(SmoothGlowstoneClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().itemTab(SmoothGlowstone.blockItem, CreativeModeTabs.BUILDING_BLOCKS, Items.AMETHYST_BLOCK);
    }
}
