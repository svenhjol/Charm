package svenhjol.charm.module;

import svenhjol.charm.block.SmoothGlowstoneBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Smelt glowstone to get smooth glowstone.")
public class SmoothGlowstone extends MesonModule {
    public static SmoothGlowstoneBlock SMOOTH_GLOWSTONE;

    @Override
    public void register() {
        SMOOTH_GLOWSTONE = new SmoothGlowstoneBlock(this);
    }
}
