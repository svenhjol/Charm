package svenhjol.charm.module.variant_chains;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = VariantChains.class)
public class VariantChainsClient extends CharmModule {

    @Override
    public void register() {
        VariantChains.CHAINS.values()
            .forEach(chains -> chains.forEach(chain -> BlockRenderLayerMap.INSTANCE.putBlock(chain, RenderType.cutout())));
    }
}
