package svenhjol.charm.feature.smooth_glowstone.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstoneClient;

public final class Registers extends RegisterHolder<SmoothGlowstoneClient> {
    public Registers(SmoothGlowstoneClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.blockItem,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.AMETHYST_BLOCK
        );
    }
}
