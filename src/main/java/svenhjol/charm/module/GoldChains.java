package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.GoldChainBlock;
import svenhjol.charm.client.GoldChainsClient;

@Module(mod = Charm.MOD_ID, client = GoldChainsClient.class, description = "Gold version of the vanilla chain.")
public class GoldChains extends CharmModule {
    public static GoldChainBlock GOLD_CHAIN;

    @Override
    public void register() {
        GOLD_CHAIN = new GoldChainBlock(this);
    }
}
