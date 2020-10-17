package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.block.RedstoneSandBlock;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "A block that acts like sand but is powered like a block of redstone.")
public class RedstoneSand extends CharmModule {
    public static RedstoneSandBlock REDSTONE_SAND;

    @Override
    public void register() {
        REDSTONE_SAND = new RedstoneSandBlock(this);
    }
}
