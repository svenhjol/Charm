package svenhjol.charm.module.redstone_sand;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_wandering_trades.ExtraWanderingTrades;

@CommonModule(mod = Charm.MOD_ID, description = "A block that acts like sand but is powered like a block of redstone.")
public class RedstoneSand extends CharmModule {
    public static RedstoneSandBlock REDSTONE_SAND;

    @Override
    public void register() {
        REDSTONE_SAND = new RedstoneSandBlock(this);
        ExtraWanderingTrades.registerItem(REDSTONE_SAND, 2, 5);
    }

}
