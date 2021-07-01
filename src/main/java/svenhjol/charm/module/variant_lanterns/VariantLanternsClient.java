package svenhjol.charm.module.variant_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = VariantLanterns.class)
public class VariantLanternsClient extends ClientModule {

    @Override
    public void register() {
        VariantLanterns.LANTERNS.values().forEach(lantern -> BlockRenderLayerMap.INSTANCE.putBlock(lantern, RenderType.cutout()));
    }
}
