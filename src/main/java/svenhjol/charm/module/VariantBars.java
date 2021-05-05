package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.CopperBarsBlock;
import svenhjol.charm.block.GoldBarsBlock;
import svenhjol.charm.client.VariantBarsClient;

@Module(mod = Charm.MOD_ID, client = VariantBarsClient.class, description = "Gold and copper versions of the vanilla iron bars.")
public class VariantBars extends CharmModule {
    public static CopperBarsBlock COPPER_BARS;
    public static GoldBarsBlock GOLD_BARS;

    @Override
    public void register() {
        COPPER_BARS = new CopperBarsBlock(this);
        GOLD_BARS = new GoldBarsBlock(this);
    }
}
