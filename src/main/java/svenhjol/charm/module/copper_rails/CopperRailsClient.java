package svenhjol.charm.module.copper_rails;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class CopperRailsClient extends CharmClientModule {
    public CopperRailsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(CopperRails.COPPER_RAIL, RenderLayer.getCutout());
    }
}