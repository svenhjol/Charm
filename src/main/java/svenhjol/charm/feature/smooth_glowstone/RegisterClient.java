package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.helper.ClientRegistryHelper;

public class RegisterClient extends Register<SmoothGlowstoneClient> {
    public RegisterClient(SmoothGlowstoneClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        ClientRegistryHelper.itemTab(SmoothGlowstone.blockItem, CreativeModeTabs.BUILDING_BLOCKS, Items.AMETHYST_BLOCK);
    }
}
