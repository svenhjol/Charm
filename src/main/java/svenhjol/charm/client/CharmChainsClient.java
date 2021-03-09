package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.CharmChains;

public class CharmChainsClient extends CharmClientModule {
    public CharmChainsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {

        BlockRenderLayerMap.INSTANCE.putBlock(CharmChains.GOLD_CHAIN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CharmChains.COPPER_CHAIN, RenderLayer.getCutout());
    }
}
