package svenhjol.charm.module.redstone_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.redstone_lanterns.RedstoneLanterns;

public class RedstoneLanternsClient extends CharmClientModule {
    public RedstoneLanternsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(RedstoneLanterns.REDSTONE_LANTERN, RenderType.cutout());
    }
}
