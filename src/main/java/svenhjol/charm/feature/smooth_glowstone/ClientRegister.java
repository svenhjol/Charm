package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.Register;

public class ClientRegister extends Register<SmoothGlowstoneClient> {
    public ClientRegister(SmoothGlowstoneClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().itemTab(SmoothGlowstone.blockItem, CreativeModeTabs.BUILDING_BLOCKS, Items.AMETHYST_BLOCK);
    }
}
