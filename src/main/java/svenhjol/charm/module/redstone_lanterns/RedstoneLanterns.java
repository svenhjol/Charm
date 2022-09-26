package svenhjol.charm.module.redstone_lanterns;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_wandering_trades.ExtraWanderingTrades;

@CommonModule(mod = Charm.MOD_ID, description = "A lantern that emits light when a redstone signal is received.")
public class RedstoneLanterns extends CharmModule {
    public static RedstoneLanternBlock REDSTONE_LANTERN;

    @Override
    public void register() {
        REDSTONE_LANTERN = new RedstoneLanternBlock(this);
        ExtraWanderingTrades.registerItem(REDSTONE_LANTERN, 1, 5);
    }
}
