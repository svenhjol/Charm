package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.GoldBarsBlock;
import svenhjol.charm.client.VariantBarsClient;

@Module(mod = Charm.MOD_ID, client = VariantBarsClient.class, description = "Gold variant of vanilla iron bars.")
public class VariantBars extends CharmModule {
    public static GoldBarsBlock GOLD_BARS;

    @Override
    public void register() {
        GOLD_BARS = new GoldBarsBlock(this);
    }
}
