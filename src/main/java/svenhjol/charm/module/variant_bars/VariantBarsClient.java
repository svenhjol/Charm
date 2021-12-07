package svenhjol.charm.module.variant_bars;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = VariantBars.class)
public class VariantBarsClient extends CharmModule {
    @Override
    public void register() {
        VariantBars.BARS.values()
            .forEach(bars -> bars.forEach(bar -> BlockRenderLayerMap.INSTANCE.putBlock(bar, RenderType.cutout())));
    }
}
