package svenhjol.charm.module.variant_bars;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class VariantBarsClient extends CharmClientModule {
    public VariantBarsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantBars.BARS.forEach(bars -> BlockRenderLayerMap.INSTANCE.putBlock(bars, RenderLayer.getCutout()));
    }
}
