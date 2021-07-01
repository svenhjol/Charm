package svenhjol.charm.module.redstone_lanterns;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = RedstoneLanterns.class)
public class RedstoneLanternsClient extends ClientModule {

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(RedstoneLanterns.REDSTONE_LANTERN, RenderType.cutout());
    }
}
