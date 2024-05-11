package svenhjol.charm.feature.smooth_glowstone.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstoneClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<SmoothGlowstoneClient> {
    public Registers(SmoothGlowstoneClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common.registers.blockItem,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.AMETHYST_BLOCK
        );
    }
}
