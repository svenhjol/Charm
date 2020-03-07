package svenhjol.charm.building.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.building.block.SmoothGlowstoneBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.BUILDING,
    description = "Smelt glowstone in a furnace to get smooth glowstone.")
public class SmoothGlowstone extends MesonModule {
    public static SmoothGlowstoneBlock block;

    @Override
    public void init() {
        block = new SmoothGlowstoneBlock(this);
    }
}
