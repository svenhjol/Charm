package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.VariantLadders;

public class VariantLaddersClient extends CharmClientModule {
    public VariantLaddersClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantLadders.LADDER_BLOCKS.values().forEach(ladder -> {
            BlockRenderLayerMap.INSTANCE.putBlock(ladder, RenderLayer.getCutout());
        });
    }
}
