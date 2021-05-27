package svenhjol.charm.module.redstone_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class RedstoneLanternsClient extends CharmClientModule {
    public RedstoneLanternsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(RedstoneLanterns.REDSTONE_LANTERN, RenderLayer.getCutout());
    }
}
