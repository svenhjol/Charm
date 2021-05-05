package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.VariantBars;

public class VariantBarsClient extends CharmClientModule {
    public VariantBarsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantBars.BARS.forEach(bars -> BlockRenderLayerMap.INSTANCE.putBlock(bars, RenderLayer.getCutout()));
    }
}
