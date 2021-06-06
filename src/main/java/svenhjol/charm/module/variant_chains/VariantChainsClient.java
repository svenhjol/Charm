package svenhjol.charm.module.variant_chains;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.variant_chains.VariantChains;

public class VariantChainsClient extends CharmClientModule {
    public VariantChainsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantChains.CHAINS.values().forEach(chain -> BlockRenderLayerMap.INSTANCE.putBlock(chain, RenderType.cutout()));
    }
}
