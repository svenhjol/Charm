package svenhjol.charm.module.variant_ladders;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = VariantLadders.class)
public class VariantLaddersClient extends CharmClientModule {
    @Override
    public void register() {
        VariantLadders.LADDER_BLOCKS.values().forEach(ladder -> BlockRenderLayerMap.INSTANCE.putBlock(ladder, RenderType.cutout()));
    }
}
