package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.RedstoneLanterns;

public class RedstoneLanternsClient extends CharmClientModule {
    public RedstoneLanternsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(RedstoneLanterns.REDSTONE_LANTERN, RenderLayer.getCutout());
    }
}
