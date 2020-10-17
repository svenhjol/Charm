package svenhjol.charm.module;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.GoldChainBlock;

@Module(mod = Charm.MOD_ID, description = "Gold version of the vanilla chain.")
public class GoldChains extends CharmModule {
    public static GoldChainBlock GOLD_CHAIN;

    @Override
    public void register() {
        GOLD_CHAIN = new GoldChainBlock(this);
    }

    @Override
    public void clientRegister() {
        BlockRenderLayerMap.INSTANCE.putBlock(GOLD_CHAIN, RenderLayer.getCutout());
    }
}
