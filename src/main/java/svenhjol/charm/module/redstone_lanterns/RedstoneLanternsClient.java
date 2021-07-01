package svenhjol.charm.module.redstone_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = RedstoneLanterns.class)
public class RedstoneLanternsClient extends CharmClientModule {

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(RedstoneLanterns.REDSTONE_LANTERN, RenderType.cutout());
    }
}
