package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.CopperChainBlock;
import svenhjol.charm.block.GoldChainBlock;
import svenhjol.charm.client.VariantChainsClient;

@Module(mod = Charm.MOD_ID, client = VariantChainsClient.class, description = "Gold and copper chains made from respective nuggets.")
public class VariantChains extends CharmModule {
    public static GoldChainBlock GOLD_CHAIN;
    public static CopperChainBlock COPPER_CHAIN;

    @Override
    public void register() {
        GOLD_CHAIN = new GoldChainBlock(this, "gold_chain");
        COPPER_CHAIN = new CopperChainBlock(this, "copper_chain");
    }
}
