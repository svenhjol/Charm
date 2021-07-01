package svenhjol.charm.module.variant_chains;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = VariantChains.class)
public class VariantChainsClient extends ClientModule {

    @Override
    public void register() {
        VariantChains.CHAINS.values().forEach(chain -> BlockRenderLayerMap.INSTANCE.putBlock(chain, RenderType.cutout()));
    }
}
