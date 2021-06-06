package svenhjol.charm.module.copper_rails;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.copper_rails.CopperRails;

public class CopperRailsClient extends CharmClientModule {
    public CopperRailsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(CopperRails.COPPER_RAIL, RenderType.cutout());
    }
}