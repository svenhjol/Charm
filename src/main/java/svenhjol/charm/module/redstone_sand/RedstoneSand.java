package svenhjol.charm.module.redstone_sand;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "A block that acts like sand but is powered like a block of redstone.")
public class RedstoneSand extends svenhjol.charm.loader.CommonModule {
    public static RedstoneSandBlock REDSTONE_SAND;

    @Override
    public void register() {
        REDSTONE_SAND = new RedstoneSandBlock(this);
    }
}
