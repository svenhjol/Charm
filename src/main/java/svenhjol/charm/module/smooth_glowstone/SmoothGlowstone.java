package svenhjol.charm.module.smooth_glowstone;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_wandering_trades.ExtraWanderingTrades;

@CommonModule(mod = Charm.MOD_ID, description = "Smooth Glowstone")
public class SmoothGlowstone extends CharmModule {
    public static SmoothGlowstoneBlock SMOOTH_GLOWSTONE;

    @Override
    public void register() {
        SMOOTH_GLOWSTONE = new SmoothGlowstoneBlock(this);
    }

    @Override
    public void runWhenEnabled() {
        ExtraWanderingTrades.registerItem(SMOOTH_GLOWSTONE, 2, 5);
    }
}
