package svenhjol.charm.module.variant_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class VariantLanternsClient extends CharmClientModule {
    public VariantLanternsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantLanterns.LANTERNS.forEach(lantern -> BlockRenderLayerMap.INSTANCE.putBlock(lantern, RenderLayer.getCutout()));
    }
}
