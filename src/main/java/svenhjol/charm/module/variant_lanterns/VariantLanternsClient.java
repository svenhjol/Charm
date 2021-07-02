package svenhjol.charm.module.variant_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = VariantLanterns.class)
public class VariantLanternsClient extends CharmModule {

    @Override
    public void register() {
        VariantLanterns.LANTERNS.values().forEach(lantern -> BlockRenderLayerMap.INSTANCE.putBlock(lantern, RenderType.cutout()));
    }
}
