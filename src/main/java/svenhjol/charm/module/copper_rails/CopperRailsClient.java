package svenhjol.charm.module.copper_rails;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = CopperRails.class)
public class CopperRailsClient extends CharmClientModule {
    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(CopperRails.COPPER_RAIL, RenderType.cutout());
    }
}