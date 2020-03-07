package svenhjol.charm.automation.module;

import svenhjol.charm.Charm;
import svenhjol.charm.automation.block.RedstoneSandBlock;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.AUTOMATION,
    description = "A block that acts like sand but is powered like a block of redstone.")
public class RedstoneSand extends MesonModule {
    public static RedstoneSandBlock block;

    @Override
    public void init() {
        block = new RedstoneSandBlock(this);
    }
}
