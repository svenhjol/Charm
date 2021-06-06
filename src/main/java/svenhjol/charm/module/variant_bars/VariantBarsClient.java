package svenhjol.charm.module.variant_bars;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.variant_bars.VariantBars;

public class VariantBarsClient extends CharmClientModule {
    public VariantBarsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantBars.BARS.values().forEach(bars -> BlockRenderLayerMap.INSTANCE.putBlock(bars, RenderType.cutout()));
    }
}
