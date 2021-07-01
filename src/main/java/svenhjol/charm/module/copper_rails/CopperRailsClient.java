package svenhjol.charm.module.copper_rails;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = CopperRails.class)
public class CopperRailsClient extends ClientModule {
    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(CopperRails.COPPER_RAIL, RenderType.cutout());
    }
}