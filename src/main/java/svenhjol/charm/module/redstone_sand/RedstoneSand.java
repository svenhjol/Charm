package svenhjol.charm.module.redstone_sand;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmCommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "A block that acts like sand but is powered like a block of redstone.")
public class RedstoneSand extends CharmCommonModule {
    public static RedstoneSandBlock REDSTONE_SAND;

    @Override
    public void register() {
        REDSTONE_SAND = new RedstoneSandBlock(this);
    }
}
