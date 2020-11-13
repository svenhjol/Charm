package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.GoldBarsBlock;
import svenhjol.charm.client.GoldBarsClient;

@Module(mod = Charm.MOD_ID, client = GoldBarsClient.class, description = "Gold variant of vanilla iron bars.")
public class GoldBars extends CharmModule {
    public static GoldBarsBlock GOLD_BARS;

    @Override
    public void register() {
        GOLD_BARS = new GoldBarsBlock(this);
    }
}
