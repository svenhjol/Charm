package svenhjol.charm.module.variant_ladders;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.variant_ladders.VariantLadders;

public class VariantLaddersClient extends CharmClientModule {
    public VariantLaddersClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantLadders.LADDER_BLOCKS.values().forEach(ladder -> BlockRenderLayerMap.INSTANCE.putBlock(ladder, RenderType.cutout()));
    }
}
