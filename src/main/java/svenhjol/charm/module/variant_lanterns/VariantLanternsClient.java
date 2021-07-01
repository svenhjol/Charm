package svenhjol.charm.module.variant_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = VariantLanterns.class)
public class VariantLanternsClient extends CharmClientModule {

    @Override
    public void register() {
        VariantLanterns.LANTERNS.values().forEach(lantern -> BlockRenderLayerMap.INSTANCE.putBlock(lantern, RenderType.cutout()));
    }
}
