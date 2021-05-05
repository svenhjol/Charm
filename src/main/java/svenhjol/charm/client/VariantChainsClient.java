package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.VariantBars;
import svenhjol.charm.module.VariantChains;

public class VariantChainsClient extends CharmClientModule {
    public VariantChainsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantChains.CHAINS.forEach(chain -> BlockRenderLayerMap.INSTANCE.putBlock(chain, RenderLayer.getCutout()));
    }
}
