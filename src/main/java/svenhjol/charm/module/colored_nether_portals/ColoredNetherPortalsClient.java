package svenhjol.charm.module.colored_nether_portals;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = ColoredNetherPortals.class)
public class ColoredNetherPortalsClient extends CharmModule {
    @Override
    public void register() {
        for (ColoredNetherPortalBlock block : ColoredNetherPortals.BLOCKS.values()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.translucent());
        }
    }
}
