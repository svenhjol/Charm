package svenhjol.charm.module.variant_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.variant_lanterns.VariantLanterns;

public class VariantLanternsClient extends CharmClientModule {
    public VariantLanternsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        VariantLanterns.LANTERNS.values().forEach(lantern -> BlockRenderLayerMap.INSTANCE.putBlock(lantern, RenderType.cutout()));
    }
}
