package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.VariantLanterns;

public class VariantLanternsClient extends CharmClientModule {
    public VariantLanternsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantLanterns.LANTERNS.forEach(lantern -> BlockRenderLayerMap.INSTANCE.putBlock(lantern, RenderLayer.getCutout()));
    }
}
